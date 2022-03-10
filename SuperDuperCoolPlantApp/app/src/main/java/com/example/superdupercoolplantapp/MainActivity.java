package com.example.superdupercoolplantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.superdupercoolplantapp.models.ViewModelMain;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView title;

    private ViewModelMain viewModel;

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

    @Override
    protected void onStart() {
        super.onStart();

        viewModel = new ViewModelProvider(this).get(ViewModelMain.class);
        logIn();
    }

    private void logIn() {
        // TODO a proper log in function
        viewModel.logIn(this, "elYonno", "SUIIIII");
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