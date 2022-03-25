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
import com.example.superdupercoolplantapp.background.models.Parameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewModelNewPlant extends ViewModel {
    public static final String TAG = "ViewModelNewPlant";

    /**
     * GET PARAMETERS
     */
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

    public Parameter getParameterByName(String name) {
        return Objects.requireNonNull(parameters.getValue())
                .stream()
                .filter(p -> p.getPlantGenus().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * POT NUMBERS
     */
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

    /**
     * NEW PARAMETER
     */
    private final MutableLiveData<Boolean> insertSuccess = new MutableLiveData<>(false);

    public void insertParameter(MainActivity mainActivity, Parameter parameter) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest request = new StringRequest(Request.Method.POST,
                APIs.INSERT_PLANT_PARAMETER,
                result -> onInsertParameterResult(parameter),
                error -> {
                    Log.e(TAG, "Error inserting new parameter: ", error);
                    insertSuccess.setValue(false);
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> param = new HashMap<>();

                param.put("genus", parameter.getPlantGenus());
                param.put("light", String.valueOf(parameter.getLight()));
                param.put("humid", String.valueOf(parameter.getHumidity()));
                param.put("temp", String.valueOf(parameter.getTemp()));

                return param;
            }
        };
        requestQueue.add(request);
    }

    private void onInsertParameterResult(Parameter parameter) {
        Objects.requireNonNull(parameters.getValue()).add(parameter);
        insertSuccess.setValue(true);
    }

    public MutableLiveData<Boolean> getInsertSuccess() {
        return insertSuccess;
    }
}
