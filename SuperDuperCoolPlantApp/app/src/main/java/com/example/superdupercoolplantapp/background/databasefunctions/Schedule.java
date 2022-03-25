package com.example.superdupercoolplantapp.background.databasefunctions;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.interfaces.ScheduleObserver;
import com.example.superdupercoolplantapp.background.models.NextScan;
import com.example.superdupercoolplantapp.background.models.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public enum Schedule {
    INSTANCE;
    public static final String TAG = "ViewModelGetNextScans";

    private final ArrayList<ScheduleObserver> observers = new ArrayList<>();

    public void addObserver(ScheduleObserver observer) {
        observers.add(observer);
        observer.updateSchedule();
    }

    public void notifyObservers() {
        observers.forEach(ScheduleObserver::updateSchedule);
    }

    public void getNextScans(MainActivity mainActivity, Plant plant, boolean lastPlant) {
        String link = APIs.GET_NEXT_SCANS + plant.getPlantID();

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, link, null,
                response -> onResponse(response, plant, lastPlant),
                error -> Log.e(TAG, "Cannot get next scans: ", error));

        requestQueue.add(request);
    }

    private void onResponse(JSONArray jsonArray, Plant plant, boolean lastPlant) {
        {
            try {
                if (jsonArray.length() != 0) {
                    JSONObject o = jsonArray.getJSONObject(0);

                    String plantName = o.getString("plantName");
                    String nextScan = o.getString("nextScan");

                    NextScan newScan = new NextScan(plant.getPlantID(), plantName, nextScan);
                    plant.setNextScan(newScan);

                    if (lastPlant) notifyObservers();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
