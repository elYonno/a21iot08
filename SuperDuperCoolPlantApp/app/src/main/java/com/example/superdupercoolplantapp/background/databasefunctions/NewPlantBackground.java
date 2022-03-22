package com.example.superdupercoolplantapp.background.databasefunctions;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.fragments.newplant.NewPlantInterface;

import org.json.JSONException;
import org.json.JSONObject;

public enum NewPlantBackground {
    INSTANCE;

    public static String TAG = "NewPlantBackground";

    public void getOptimalValues(MainActivity mainActivity, String plantGenus, NewPlantInterface newPlant) {
        String api = APIs.GET_OPTIMAL_CONDITIONS + plantGenus;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, api, null,
                response -> {
                    try {
                        JSONObject object = response.getJSONObject(0);

                        double optimalLight = object.getDouble("optimalLight");
                        double optimalHumidity = object.getDouble("optimalHumidity");
                        double optimalTemp = object.getDouble("optimalTemp");

                        newPlant.setOptimalFields(optimalLight, optimalHumidity, optimalTemp);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON error:", e);
                    }
                }, error -> {
                    Toast.makeText(mainActivity, "Error getting optimal values", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error getting optimal values: ", error);
        });

        requestQueue.add(request);
    }
}
