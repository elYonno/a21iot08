package com.example.superdupercoolplantapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.superdupercoolplantapp.background.viewmodels.ViewModelAccount;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelNextScans;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelRecentReadings;
import com.example.superdupercoolplantapp.background.models.AccountModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView title;

    private ViewModelAccount viewModelAccount;
    private ViewModelRecentReadings viewModelRecentReadings;
    private ViewModelNextScans viewModelGetNextScans;

    private AccountModel loggedInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up titles
        title = findViewById(R.id.text_title);

        // set up bottom navigation menu
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

        viewModelAccount = new ViewModelProvider(this).get(ViewModelAccount.class);
        viewModelAccount.getLoggedInAccount().observe(this, this::updateAccount);
        viewModelRecentReadings = new ViewModelProvider(this).get(ViewModelRecentReadings.class);
        viewModelGetNextScans = new ViewModelProvider(this).get(ViewModelNextScans.class);

        logIn();
    }

    private void updateAccount(AccountModel accountModel) {
        loggedInAccount = accountModel;
    }

    public AccountModel getAccount() { return loggedInAccount; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void logIn() {
        // TODO a proper log in function
        viewModelAccount.logIn(this, "elYonno", "SUIIIII");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getMainData(int userID) {
        viewModelRecentReadings.getRecentReadings(this, userID);
        viewModelGetNextScans.getNextScans(this, userID);
    }

    public void setText(String text) {
        title.setText(text);
    }

    public void showBottomNav() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNav() {
        bottomNavigationView.setVisibility(View.GONE);
    }
}