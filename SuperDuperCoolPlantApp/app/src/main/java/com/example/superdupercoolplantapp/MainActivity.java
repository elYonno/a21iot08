package com.example.superdupercoolplantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelAccount;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;
import com.example.superdupercoolplantapp.background.models.AccountModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView title;
    private SwipeRefreshLayout refreshLayout;

    private ViewModelAccount viewModelAccount;
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

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() ->
                viewModelMyPlants.queryPlants(MainActivity.this, loggedInAccount.getUserID()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModelAccount = new ViewModelProvider(this).get(ViewModelAccount.class);
        viewModelAccount.getLoggedInAccount().observe(this, this::updateAccount);

        viewModelMyPlants = new ViewModelProvider(this).get(ViewModelMyPlants.class);

        logIn();
    }

    private void updateAccount(AccountModel accountModel) {
        loggedInAccount = accountModel;
        viewModelMyPlants.queryPlants(this, loggedInAccount.getUserID());
    }

    public AccountModel getAccount() { return loggedInAccount; }

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

    public void setRefreshEnabled(boolean enabled) {
        refreshLayout.setEnabled(enabled);
    }

    public void stopRefreshAnimation() {
        refreshLayout.setRefreshing(false);
    }
}