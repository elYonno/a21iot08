package com.example.superdupercoolplantapp.background.databasefunctions;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.interfaces.NewPlantInterface;
import com.example.superdupercoolplantapp.background.models.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewModelMyPlants extends ViewModel {
    public static final String TAG = "ViewModelMyPlants";

    private final MutableLiveData<ArrayList<Plant>> plants = new MutableLiveData<>();

    public void queryPlants(MainActivity mainActivity, int userID) {
        String link = APIs.GET_PLANTS + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, link, null,
                response -> queryResponse(response, mainActivity),
                error -> Log.e(TAG, "Error getting plants: ", error));
        requestQueue.add(request);
    }

    private void queryResponse(JSONArray jsonArray, MainActivity mainActivity) {
        try {
            ArrayList<Plant> plants = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);

                int plantID = o.getInt("plantId");
                String plantName = o.getString("plantName");
                String plantType = o.getString("plantType");
                int potNumber = o.getInt("potNumber");
                String photo = o.getString("photo");

                Plant newPlant = new Plant(plantID, potNumber, plantName, plantType, photo);
                plants.add(newPlant);

                boolean lastElement = i == jsonArray.length() - 1;

                Readings.INSTANCE.queryRecentReadings(mainActivity, newPlant, lastElement);
                Schedule.INSTANCE.getNextScans(mainActivity, newPlant, lastElement);
            }

            this.plants.setValue(plants);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<ArrayList<Plant>> getPlants() {
        return plants;
    }

    public Plant getPlantByID(int id) {
        return Objects.requireNonNull(plants.getValue())
                .stream()
                .filter(t -> t.getPlantID() == id)
                .findFirst()
                .orElse(null);
    }

    public void insertPlant(MainActivity mainActivity, NewPlantInterface newPlantInterface,
                            String name, String genus, int potNumber, int userId, String image) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

        StringRequest request = new StringRequest(Request.Method.POST,
                APIs.INSERT_NEW_PLANT,
                response -> onNewPlantResponse(mainActivity, userId, newPlantInterface), // refresh
                error -> {
                    Log.e(TAG, "Error inserting new plant", error);
                    newPlantInterface.response(false);
                }
                ) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>(4);

                params.put("name", name);
                params.put("genus", genus);
                params.put("pot", String.valueOf(potNumber));
                params.put("user", String.valueOf(userId));
                params.put("image", image);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private void onNewPlantResponse(MainActivity mainActivity, int userId, NewPlantInterface newPlantInterface) {
        queryPlants(mainActivity, userId);
        newPlantInterface.response(true);
    }
}
