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

import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelAccount;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelMyPlants;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelReadings;
import com.example.superdupercoolplantapp.background.models.AccountModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView title;

    private ViewModelAccount viewModelAccount;
    private ViewModelReadings viewModelReadings;
    private ViewModelMyPlants viewModelMyPlants;

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

        viewModelReadings = new ViewModelProvider(this).get(ViewModelReadings.class);

        viewModelMyPlants = new ViewModelProvider(this).get(ViewModelMyPlants.class);
        viewModelMyPlants.getPlants().observe(this, this::updatePlant);

        logIn();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePlant(ArrayList<Plant> plants) {
        for (Plant plant : plants) {
            viewModelReadings.queryRecentReadings(this, plant);
        }
    }

    private void updateAccount(AccountModel accountModel) {
        loggedInAccount = accountModel;
        viewModelMyPlants.getPlants(this, loggedInAccount.getUserID());
    }

    public AccountModel getAccount() { return loggedInAccount; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void logIn() {
        // TODO a proper log in function
        viewModelAccount.logIn(this, "elYonno", "SUIIIII");
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