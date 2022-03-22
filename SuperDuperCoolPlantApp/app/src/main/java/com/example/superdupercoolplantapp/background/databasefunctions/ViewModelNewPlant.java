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
import com.example.superdupercoolplantapp.background.models.Parameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ViewModelNewPlant extends ViewModel {
    public static final String TAG = "ViewModelNewPlant";

    private final MutableLiveData<ArrayList<Parameter>> parameters = new MutableLiveData<>();

    public void queryParameters(MainActivity mainActivity) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, APIs.GET_PLANT_PARAMETERS, null,
                this::parametersResponse,
                error -> Log.e(TAG, "Unable to query parameters", error));

        requestQueue.add(request);
    }

    private void parametersResponse(JSONArray jsonArray) {
        try {
            ArrayList<Parameter> newParam = new ArrayList<>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);

                int id = o.getInt("plantType");
                String type = o.getString("plantGenus");
                double light = o.getDouble("optimalLight");
                double humidity = o.getDouble("optimalHumidity");
                double temp = o.getDouble("optimalTemp");

                Parameter parameter = new Parameter(id, type, light, humidity, temp);
                newParam.add(parameter);
            }

            parameters.setValue(newParam);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: ", e);
        }
    }

    public MutableLiveData<ArrayList<Parameter>> getParameters() {
        return parameters;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Parameter getParameterByName(String name) {
        return Objects.requireNonNull(parameters.getValue())
                .stream()
                .filter(p -> p.getPlantGenus().equals(name))
                .findFirst()
                .orElse(null);
    }

    private ArrayList<Integer> smartPotNumbers;

    public void queryPotNumbers(MainActivity mainActivity) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, APIs.GET_POT_NUMBERS, null,
                response -> {
                    try {
                        smartPotNumbers = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject o = response.getJSONObject(i);
                            smartPotNumbers.add(o.getInt("potNumber"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON error: ", e);
                    }
                }, error -> Log.e(TAG, "Unable to query pot numbers: ", error));

        requestQueue.add(request);
    }

    public boolean checkPotNumberExists(int pot) {
        return smartPotNumbers.contains(pot);
    }
}
