package com.example.superdupercoolplantapp.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelNewPlant;
import com.example.superdupercoolplantapp.background.models.Parameter;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;

import java.util.ArrayList;
public class NewPlant extends Fragment {

    private MainActivity activity;
    private NavController navController;
    private ViewModelMyPlants viewModelMyPlants;
    private ViewModelNewPlant viewModelNewPlant;

    private EditText plantName, potNumber, plantTemperature, plantHumidity, plantLight;
    private TextView newTypeMessage, potNumberError;
    private AutoCompleteTextView plantType;
    private ImageView camera;

    private Plant plant;
    private Parameter parameter;
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
        newTypeMessage = view.findViewById(R.id.new_plant_type_warning);
        potNumberError = view.findViewById(R.id.new_plant_pot_number_error);

        camera = view.findViewById(R.id.new_plant_image);
        camera.setOnClickListener(this::camera_onClick);

        Button confirm = view.findViewById(R.id.new_plant_add);
        confirm.setOnClickListener(this::confirm_onClick);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModelMyPlants = new ViewModelProvider(activity).get(ViewModelMyPlants.class);

        viewModelNewPlant = new ViewModelProvider(activity).get(ViewModelNewPlant.class);
        viewModelNewPlant.queryParameters(activity);
        viewModelNewPlant.queryPotNumbers(activity);
        viewModelNewPlant.getParameters().observe(activity, this::onParametersChange);

        plantType.setOnFocusChangeListener(this::onPlantTypeChange);
        potNumber.setOnFocusChangeListener(this::onPotNumberChange);

        int plantId = NewPlantArgs.fromBundle(getArguments()).getIdPlant();
        if (plantId != -1) {
            editMode = true;
            activity.setText(String.valueOf(getResources().getText(R.string.edit_plant)));
            plant = viewModelMyPlants.getPlantByID(plantId);
            populateFields();
        } else {
            editMode = false;
            activity.setText(String.valueOf(getResources().getText(R.string.new_plant)));
        }

        activity.hideBottomNav();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onParametersChange(ArrayList<Parameter> parameters) {
        if (editMode) { // insert values if edit mode
            Parameter newParameter = viewModelNewPlant.getParameterByName(plant.getPlantType());

            if (newParameter != null) {
                parameter = newParameter;
                plantLight.setText(String.valueOf(newParameter.getLight()));
                plantHumidity.setText(String.valueOf(newParameter.getHumidity()));
                plantTemperature.setText(String.valueOf(newParameter.getTemp()));
            }
        }
        String[] array = parameters.stream()
                .map(Parameter::getPlantGenus)
                .toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,
                array);

        plantType.setAdapter(adapter);
    }

    private void confirm_onClick(View view) {
        navController.popBackStack();
    }

    private void camera_onClick(View view) {

    }

    private void populateFields() {
        plantName.setText(plant.getPlantName());
        plantType.setText(plant.getPlantType());
        potNumber.setText(String.valueOf(plant.getPotNumber()));
        camera.setImageBitmap(Base64Tool.decodeImage(plant.getImage()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onPlantTypeChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            String newText = plantType.getText().toString();

            parameter = viewModelNewPlant.getParameterByName(newText);

            if (parameter == null) { // new plant type?
                if (!newText.equals("")) {
                    String message = String.format("Creating a new plant type called %s.", newText);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    newTypeMessage.setVisibility(View.VISIBLE);

                    plantTemperature.setEnabled(true);
                    plantTemperature.setText("");

                    plantHumidity.setEnabled(true);
                    plantHumidity.setText("");

                    plantLight.setEnabled(true);
                    plantLight.setText("");
                } else {
                    plantTemperature.setEnabled(false);
                    plantHumidity.setEnabled(false);
                    plantLight.setEnabled(false);
                }
            } else {
                newTypeMessage.setVisibility(View.GONE);

                plantTemperature.setEnabled(false);
                plantTemperature.setText(String.valueOf(parameter.getTemp()));

                plantHumidity.setEnabled(false);
                plantHumidity.setText(String.valueOf(parameter.getHumidity()));

                plantLight.setEnabled(false);
                plantLight.setText(String.valueOf(parameter.getLight()));
            }
        }
    }

    private void onPotNumberChange(View view, boolean hasFocus) {
        if (!hasFocus) {
           String number = potNumber.getText().toString();

           if (!number.equals("")) {
               if (viewModelNewPlant.checkPotNumberExists(Integer.parseInt(number))) // all good
                   potNumberError.setVisibility(View.GONE);
               else
                   potNumberError.setVisibility(View.VISIBLE);
           }
        }
    }
}