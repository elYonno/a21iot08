package com.example.superdupercoolplantapp.fragments.newplant;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.databasefunctions.NewPlantBackground;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;

public class NewPlant extends Fragment implements NewPlantInterface {

    private MainActivity activity;
    private NavController navController;
    private ViewModelMyPlants viewModel;

    private TextView plantName, potNumber, plantTemperature, plantHumidity, plantLight;
    private AutoCompleteTextView plantType;
    private ImageView camera;
    private Button confirm;

    private Plant plant;
    private boolean editMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_plant, container, false);
        activity = (MainActivity) requireActivity();

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

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(activity).get(ViewModelMyPlants.class);

        int plantId = NewPlantArgs.fromBundle(getArguments()).getIdPlant();
        if (plantId != -1) {
            editMode = true;
            activity.setText(String.valueOf(getResources().getText(R.string.edit_plant)));
            plant = viewModel.getPlantByID(plantId);
            populateFields();
        } else {
            editMode = false;
            activity.setText(String.valueOf(getResources().getText(R.string.new_plant)));
        }

        activity.hideBottomNav();
    }

    private void populateFields() {
        NewPlantBackground.INSTANCE.getOptimalValues(activity, plant.getPlantType(), this);
        plantName.setText(plant.getPlantName());
        plantType.setText(plant.getPlantType());
        potNumber.setText(String.valueOf(plant.getPotNumber()));
        camera.setImageBitmap(Base64Tool.decodeImage(plant.getImage()));

    }

    @Override
    public void setOptimalFields(double optimalLight, double optimalHumidity, double optimalTemp) {
        plantLight.setText(String.valueOf(optimalLight));
        plantHumidity.setText(String.valueOf(optimalHumidity));
        plantTemperature.setText(String.valueOf(optimalTemp));
    }

    private void camera_onClick(View view) {

    }

    private void confirm_onClick(View view) {
        navController.popBackStack();
    }
}