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
import com.example.superdupercoolplantapp.background.models.Reading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewModelRecentReadings extends ViewModel {
    private static final String TAG = "ViewModelRecentReadings";

    private final MutableLiveData<ArrayList<Reading>> readings = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getRecentReadings(MainActivity mainActivity, int userID) {
        String readings = APIs.RECENT_READINGS + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, readings, null,
                response -> {
                    try {
                        ArrayList<Reading> newReadings = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);

                            if (object == null) throw new JSONException(TAG);
                            else {
                                int plantID = object.getInt("plantID");
                                String plantName = object.getString("plantName");
                                String timestamp = object.getString("timestamp");
                                double lightValue = object.getDouble("lightValue");
                                double humidityValue = object.getDouble("humidityValue");
                                double tempValue = object.getDouble("tempValue");

                                Reading newReading = new Reading(plantID, plantName, timestamp, lightValue,
                                        humidityValue, tempValue);

                                double deltaLight = object.getDouble("deltaLight");
                                double deltaHumidity = object.getDouble("deltaHumidity");
                                double deltaTemp = object.getDouble("deltaTemp");

                                newReading.setEmotions(deltaLight, deltaHumidity, deltaTemp);
                                newReadings.add(newReading);
                            }
                        }

                        setReadings(newReadings);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Failure getting recent readings.", error));

        requestQueue.add(request);
    }

    public void setReadings(ArrayList<Reading> readings) {
        this.readings.setValue(readings);
    }

    public MutableLiveData<ArrayList<Reading>> getReadings() {
        return readings;
    }
}
