package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;

public class NewPlant extends Fragment {

    private MainActivity activity;
    private NavController navController;

    private TextView plantName, potNumber, plantTemperature, plantHumidity, plantLight;
    private AutoCompleteTextView plantType;
    private ImageView camera;
    private Button confirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_plant, container, false);
        activity = (MainActivity) requireActivity();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plantName = view.findViewById(R.id.new_plant_name);
        plantType = view.findViewById(R.id.new_plant_type);
        potNumber = view.findViewById(R.id.new_plant_smartpot);
        plantTemperature = view.findViewById(R.id.new_plant_temperature);
        plantHumidity = view.findViewById(R.id.new_plant_humidity);
        plantLight = view.findViewById(R.id.new_plant_light);

        camera = view.findViewById(R.id.new_plant_image);
        camera.setOnClickListener(this::camera_onClick);

        confirm = view.findViewById(R.id.new_plant_add);
        confirm.setOnClickListener(this::confirm_onClick);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.new_plant));
        activity.hideBottomNav();
    }

    private void camera_onClick(View view) {

    }

    private void confirm_onClick(View view) {
        navController.popBackStack();
    }
}