package com.example.superdupercoolplantapp.background.viewmodels;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewModelNextScans extends ViewModel {
    public static final String TAG = "ViewModelGetNextScans";

    private MutableLiveData<ArrayList<NextScan>> nextScans = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getNextScans(MainActivity mainActivity, int userID) {
        String link = APIs.GET_NEXT_SCANS + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, link, null,
                this::response, error -> Log.e(TAG, "Cannot get next scans: ", error));

        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void response(JSONArray jsonArray) {
        {
            try {
                ArrayList<NextScan> newNextScans = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);

                    int plantID = o.getInt("plantID");
                    String plantName = o.getString("plantName");
                    int potNumber = o.getInt("potNumber");
                    String nextScan = o.getString("nextScan");

                    NextScan newScan = new NextScan(plantID, potNumber, plantName, nextScan);
                    newNextScans.add(newScan);
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
