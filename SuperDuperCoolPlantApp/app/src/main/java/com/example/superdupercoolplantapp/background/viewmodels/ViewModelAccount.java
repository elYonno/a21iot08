package com.example.superdupercoolplantapp.background.viewmodels;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.background.APIs;
import com.example.superdupercoolplantapp.background.models.AccountModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewModelAccount extends ViewModel {
    private static final String TAG = "ViewModelAccount";

    private final MutableLiveData<AccountModel> loggedInAccount = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void logIn(MainActivity mainActivity, String username, String password) {
        String logIn = APIs.LOG_IN + username + "/" + password;

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, logIn, null,
            response -> {
                try {
                    JSONObject o = response.getJSONObject(0);

                    int userID = o.getInt("userID");
                    String realName = o.getString("realName");
                    String phoneNumber = o.getString("phoneNumber");
                    String email = o.getString("emailAddress");

                    updateUser(new AccountModel(userID, username, realName, phoneNumber, email, password));
                    mainActivity.getMainData(userID);
                } catch (JSONException e) {
                    Toast.makeText(mainActivity, "Incorrect username/password.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(mainActivity, "Server error.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Unable to connect to server: ", error);
        });

        requestQueue.add(request);
    }

    public void updateLogIn(MainActivity mainActivity, AccountModel user) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest request = new StringRequest(Request.Method.POST, APIs.UPDATE_LOGIN, response -> updateUser(user),
                error -> {
                    Toast.makeText(mainActivity, "Error on updating user data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error posting: " + error);
                }) {
                    @NonNull
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("namereal", user.getRealName());
                        params.put("phone", user.getPhoneNumber());
                        params.put("email", user.getEmailAddress());
                        params.put("pass", user.getPassword());
                        params.put("id", String.valueOf(user.getUserID()));
                        return params;
                    }
        };
        requestQueue.add(request);
    }

    public MutableLiveData<AccountModel> getLoggedInAccount() {
        return loggedInAccount;
    }

    private void updateUser(AccountModel user) {
        loggedInAccount.setValue(user);
    }

}
