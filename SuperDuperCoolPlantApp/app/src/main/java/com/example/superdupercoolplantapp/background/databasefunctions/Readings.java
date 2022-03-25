package com.example.superdupercoolplantapp.background.databasefunctions;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.interfaces.ReadingsObserver;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public enum Readings {
    INSTANCE;
    private static final String TAG = "ViewModelRecentReadings";

    private final ArrayList<ReadingsObserver> observers = new ArrayList<>();


    public void addObserver(ReadingsObserver newObserver) {
        observers.add(newObserver);
        newObserver.updateReadings();
    }

    public void notifyObservers() {
        observers.forEach(ReadingsObserver::updateReadings);
    }

    public void queryRecentReadings(MainActivity mainActivity, Plant plant, boolean lastPlant) {
        String readings = APIs.RECENT_READINGS + plant.getPlantID();

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, readings, null,
                response -> queryResponse(response, plant, lastPlant),
                error -> Log.e(TAG, "Failure getting recent readings.", error));

        requestQueue.add(request);
    }

    private void queryResponse(JSONArray response, Plant plant, boolean lastPlant) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);

                if (object == null) throw new JSONException(TAG);
                else {
                    String timestamp = object.getString("timestamp");
                    double lightValue = object.getDouble("lightValue");
                    double humidityValue = object.getDouble("humidityValue");
                    double tempValue = object.getDouble("tempValue");

                    Reading newReading = new Reading(plant.getPlantID(), plant.getPlantName(),
                            timestamp, lightValue, humidityValue, tempValue);

                    double deltaLight = object.getDouble("deltaLight");
                    double deltaHumidity = object.getDouble("deltaHumidity");
                    double deltaTemp = object.getDouble("deltaTemp");

                    newReading.setEmotions(deltaLight, deltaHumidity, deltaTemp);
                    plant.addReading(newReading);
                }

                if (lastPlant) notifyObservers();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
