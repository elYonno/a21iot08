package com.example.superdupercoolplantapp.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewModelMain extends ViewModel {
    private static final String logInAPI = "https://studev.groept.be/api/a21iot08/logIn/";
    private static final String TAG = "ViewModelMain";

    private final MutableLiveData<AccountModel> loggedInAccount = new MutableLiveData<>();

    public void logIn(Context context, String username, String password) {
        String logIn = logInAPI + username + "/" + password;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, logIn, null,
            response -> {
                try {
                    JSONObject o = response.getJSONObject(0);

                    int userID = o.getInt("userID");
                    String realName = o.getString("realName");
                    String phoneNumber = o.getString("phoneNumber");
                    String email = o.getString("emailAddress");

                    updateUser(new AccountModel(userID, username, realName, phoneNumber, email, password));

                } catch (JSONException e) {
                    Toast.makeText(context, "Incorrect username/password.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(context, "Server error.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Unable to connect to server: ", error);
        });

        requestQueue.add(request);
    }

    public MutableLiveData<AccountModel> getLoggedInAccount() {
        return loggedInAccount;
    }

    private void updateUser(AccountModel user) {
        loggedInAccount.setValue(user);
    }

}
