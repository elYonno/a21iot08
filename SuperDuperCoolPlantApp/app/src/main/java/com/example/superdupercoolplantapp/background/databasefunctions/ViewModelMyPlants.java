package com.example.superdupercoolplantapp.background.databasefunctions;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.models.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ViewModelMyPlants extends ViewModel {
    public static final String TAG = "ViewModelMyPlants";

    private final MutableLiveData<ArrayList<Plant>> plants = new MutableLiveData<>();

    public void queryPlants(MainActivity mainActivity, int userID) {
        String link = APIs.GET_PLANTS + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, link, null,
                this::response, error -> Log.e(TAG, "Error getting plants: ", error));
        requestQueue.add(request);
    }

    private void response(JSONArray jsonArray) {
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
            }

            this.plants.setValue(plants);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<ArrayList<Plant>> getPlants() {
        return plants;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Plant getPlantByID(int id) {
        return Objects.requireNonNull(plants.getValue())
                .stream()
                .filter(t -> t.getPlantID() == id)
                .findFirst()
                .orElse(null);
    }
}
