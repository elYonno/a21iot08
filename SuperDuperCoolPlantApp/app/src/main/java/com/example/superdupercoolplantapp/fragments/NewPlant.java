package com.example.superdupercoolplantapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;
public class NewPlant extends Fragment {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> storageLauncher;

    private MainActivity activity;
    private NavController navController;
    private ViewModelMyPlants viewModelMyPlants;
    private ViewModelNewPlant viewModelNewPlant;

    private EditText plantName, potNumber, plantTemperature, plantHumidity, plantLight;
    private TextView newTypeMessage, potNumberError;
    private AutoCompleteTextView plantType;
    private ImageView camera;
    private Button confirm;

    private Plant plant;
    private Parameter parameter;
    private boolean editMode;
    private boolean ready;
    private Bitmap profilePicture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            camera.setImageBitmap(bitmap);
                            profilePicture = bitmap;
                        } else
                            Toast.makeText(activity, "Unable to get image.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Unable to start camera.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        storageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri image = result.getData().getData();
                            camera.setImageURI(image);
                            try {
                                profilePicture = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(activity, "Unable to get image.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Unable to start gallery.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_plant, container, false);
        activity = (MainActivity) requireActivity();
        ready = false;

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

        confirm = view.findViewById(R.id.new_plant_add);
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
        viewModelNewPlant.getInsertSuccess().observe(activity, this::parameterInsertSuccess);

        plantType.setOnFocusChangeListener(this::onPlantTypeChange);
        potNumber.setOnFocusChangeListener(this::onPotNumberChange);

        int plantId = NewPlantArgs.fromBundle(getArguments()).getIdPlant();
        if (plantId != -1) {
            editMode = true;
            activity.setText(String.valueOf(getResources().getText(R.string.edit_plant)));
            confirm.setText(getResources().getText(R.string.edit_plant));
            plant = viewModelMyPlants.getPlantByID(plantId);
            populateFields();
        } else editMode = false;

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
        if (validInput()) {
            ready = true;

            if (parameter == null) { // create new plant parameter
                String genusName = plantType.getText().toString();
                double doubleTemp = Double.parseDouble(plantTemperature.getText().toString());
                double doubleHumidity = Double.parseDouble(plantHumidity.getText().toString());
                double doubleLight = Double.parseDouble(plantLight.getText().toString());

                parameter = new Parameter(-1, genusName, doubleLight, doubleHumidity, doubleTemp);
                viewModelNewPlant.insertParameter(activity, parameter);
            } else parameterInsertSuccess(true);
        }
    }

    private void parameterInsertSuccess(Boolean result) {
        if (ready)
            if (result) {
                if (!editMode) {
                    String stringName = plantName.getText().toString();
                    String stringGenus = plantType.getText().toString();
                    int intPotNumber = Integer.parseInt(potNumber.getText().toString());
                    String image = null;
                    if (profilePicture != null) image = Base64Tool.encodeImage(profilePicture);

                    viewModelMyPlants.insertPlant(activity, stringName, stringGenus, intPotNumber,
                            activity.getAccount().getUserID(), image);
                    navController.popBackStack();
                }
            } else Toast.makeText(activity, "Failure inserting a new plant type.", Toast.LENGTH_SHORT).show();
    }

    private boolean validInput() {
        if (plantName.getText().toString().equals("") ||
                plantType.getText().toString().equals("") ||
                potNumber.getText().toString().equals("") ||
                plantTemperature.getText().toString().equals("") ||
                plantHumidity.getText().toString().equals("") ||
                plantLight.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill in all forms.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (potNumberError.getVisibility() == View.VISIBLE) {
            Toast.makeText(activity, "Please give a valid SmartPot number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }

    private void camera_onClick(View view) {
         @SuppressLint("UseCompatLoadingForDrawables") AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Choose image")
                .setPositiveButton("Camera", (dialogInterface, i) -> {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraLauncher.launch(takePicture);
                })
                .setNegativeButton("Storage", (dialogInterface, i) -> {
                    Intent getPicture = new Intent(Intent.ACTION_PICK);
                    getPicture.setType("image/*");
                    storageLauncher.launch(getPicture);
                })
                .setNeutralButton("Remove", (dialogInterface, i) -> {
                    profilePicture = null;
                    camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
                    dialogInterface.dismiss();
                })
                 .create();
         dialog.show();
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