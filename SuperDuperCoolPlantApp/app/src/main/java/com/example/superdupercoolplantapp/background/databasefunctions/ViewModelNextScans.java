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
import com.example.superdupercoolplantapp.background.models.NextScan;
import com.example.superdupercoolplantapp.background.models.Plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewModelNextScans extends ViewModel {
    public static final String TAG = "ViewModelGetNextScans";

    private final MutableLiveData<ArrayList<NextScan>> nextScans = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getNextScans(MainActivity mainActivity, Plant plant) {
        String link = APIs.GET_NEXT_SCANS + plant.getPlantID();

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, link, null,
                response -> onResponse(response, plant),
                error -> Log.e(TAG, "Cannot get next scans: ", error));

        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onResponse(JSONArray jsonArray, Plant plant) {
        {
            try {
                ArrayList<NextScan> newNextScans = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);

                    String plantName = o.getString("plantName");
                    String nextScan = o.getString("nextScan");

                    NextScan newScan = new NextScan(plant.getPlantID(), plantName, nextScan);
                    newNextScans.add(newScan);
                    plant.setNextScan(newScan);
                }

                updateNextScans(newNextScans);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateNextScans(ArrayList<NextScan> nextScans) {
        this.nextScans.setValue(nextScans);
    }

    public MutableLiveData<ArrayList<NextScan>> getNextScans() {
        return nextScans;
    }
}
