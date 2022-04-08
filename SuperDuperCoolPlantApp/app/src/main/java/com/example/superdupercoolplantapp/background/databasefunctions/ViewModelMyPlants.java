package com.example.superdupercoolplantapp.background.databasefunctions;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.interfaces.PlantInterface;
import com.example.superdupercoolplantapp.background.interfaces.PlantParameterInterface;
import com.example.superdupercoolplantapp.background.models.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            mainActivity.stopRefreshAnimation();
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

    public void insertPlant(MainActivity mainActivity, PlantInterface plantInterface,
                            String name, String genus, int potNumber, int userId, String image) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

        StringRequest request = new StringRequest(Request.Method.POST,
                APIs.INSERT_NEW_PLANT,
                response -> onNewPlantResponse(mainActivity, userId, plantInterface), // refresh
                error -> {
                    Log.e(TAG, "Error inserting new plant", error);
                    plantInterface.plantResponse(false);
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

    private void onNewPlantResponse(MainActivity mainActivity, int userId, PlantInterface plantInterface) {
        queryPlants(mainActivity, userId);
        plantInterface.plantResponse(true);
    }

    public void updatePlant(MainActivity activity, PlantInterface plantInterface, Plant plant) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest request = new StringRequest(Request.Method.POST,
                APIs.EDIT_PLANT,
                response -> plantInterface.plantResponse(true),
                error -> {
                    Log.e(TAG, "Error edit plant: ", error);
                    plantInterface.plantResponse(false);
                }
                ) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>(5);

                params.put("name", plant.getPlantName());
                params.put("genus", plant.getPlantType());
                params.put("pot", String.valueOf(plant.getPotNumber()));
                params.put("image", plant.getImage());
                params.put("id", String.valueOf(plant.getPlantID()));

                return params;
            }
        };

        requestQueue.add(request);
    }

    public void deletePlant(MainActivity activity, Plant plant, PlantInterface plantInterface) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest request = new StringRequest(Request.Method.POST,
                APIs.DELETE_PLANT,
                response -> onDeletePlant(plantInterface, plant),
                error -> {
                    Log.e(TAG, "Error deleting plant: ", error);
                    plantInterface.plantResponse(false);
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", String.valueOf(plant.getPlantID()));

                return params;
            }
        };

        requestQueue.add(request);
    }

    private void onDeletePlant(PlantInterface plantInterface, Plant plant) {
        plantInterface.plantResponse(true);
        ArrayList<Plant> newPlants = Objects.requireNonNull(plants.getValue())
                .stream()
                .filter(p -> p.getPlantID() != plant.getPlantID())
                .collect(Collectors.toCollection(ArrayList::new));
        plants.setValue(newPlants);
    }

    public void getPlantParameterByTypeName(MainActivity mainActivity, PlantParameterInterface page, String genus) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, APIs.getPlantParameter(genus), null,
                response -> {
                    try {
                        JSONObject object = response.getJSONObject(0);
                        page.plantParameter(
                                object.getDouble("optimalLight"),
                                object.getInt("waterEveryHour"),
                                object.getDouble("optimalTemp")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(mainActivity, "Failure getting optimal values.", Toast.LENGTH_SHORT).show();
            page.plantParameter(-1,-1,-1);
        });

        requestQueue.add(request);
    }
}
