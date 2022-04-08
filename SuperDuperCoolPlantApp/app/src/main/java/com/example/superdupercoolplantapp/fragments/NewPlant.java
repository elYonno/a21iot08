package com.example.superdupercoolplantapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelNewPlant;
import com.example.superdupercoolplantapp.background.interfaces.PlantInterface;
import com.example.superdupercoolplantapp.background.models.Parameter;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;

import java.io.IOException;
import java.util.ArrayList;

public class NewPlant extends Fragment implements PlantInterface {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> storageLauncher;

    private MainActivity activity;
    private NavController navController;
    private ViewModelMyPlants viewModelMyPlants;
    private ViewModelNewPlant viewModelNewPlant;

    private EditText plantName, potNumber, plantTemperature, waterSchedule, plantLight;
    private TextView newTypeMessage, potNumberError;
    private AutoCompleteTextView plantType;
    private ImageView camera;
    private Button confirm;
    private ProgressBar progressBar;

    private com.example.superdupercoolplantapp.background.models.Plant plant;
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
        waterSchedule = view.findViewById(R.id.new_plant_water);
        plantLight = view.findViewById(R.id.new_plant_light);
        newTypeMessage = view.findViewById(R.id.new_plant_type_warning);
        potNumberError = view.findViewById(R.id.new_plant_pot_number_error);
        progressBar = view.findViewById(R.id.new_plant_progress_bar);

        camera = view.findViewById(R.id.new_plant_image);
        camera.setOnClickListener(this::camera_onClick);

        confirm = view.findViewById(R.id.new_plant_add);
        confirm.setOnClickListener(this::confirm_onClick);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModelMyPlants = new ViewModelProvider(activity).get(ViewModelMyPlants.class);

        viewModelNewPlant = new ViewModelProvider(activity).get(ViewModelNewPlant.class);
        viewModelNewPlant.queryParameters(activity);
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
            viewModelNewPlant.queryPotNumbers(activity, plant.getPotNumber());
            populateFields();
        } else {
            editMode = false;
            viewModelNewPlant.queryPotNumbers(activity, -1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.hideBottomNav();
        activity.setRefreshEnabled(false);
    }

    private void onParametersChange(ArrayList<Parameter> parameters) {
        if (editMode) { // insert values if edit mode
            Parameter newParameter = viewModelNewPlant.getParameterByName(plant.getPlantType());

            if (newParameter != null) {
                parameter = newParameter;
                plantLight.setText(String.valueOf(newParameter.getLight()));
                waterSchedule.setText(String.valueOf(newParameter.getNextWaterHour()));
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
                int waterEvery = Integer.parseInt(waterSchedule.getText().toString());
                double doubleLight = Double.parseDouble(plantLight.getText().toString());

                parameter = new Parameter(-1, genusName, doubleLight, waterEvery, doubleTemp);
                viewModelNewPlant.insertParameter(activity, parameter);
            } else parameterInsertSuccess(true);
        }
    }

    private void parameterInsertSuccess(Boolean result) {
        if (ready)
            if (result) {
                String stringName = plantName.getText().toString();
                String stringGenus = plantType.getText().toString();
                int intPotNumber = Integer.parseInt(potNumber.getText().toString());

                String image = "";
                if (profilePicture != null) image = Base64Tool.encodeImage(profilePicture);

                if (!editMode) {
                    viewModelMyPlants.insertPlant(activity, this, stringName, stringGenus, intPotNumber,
                            activity.getAccount().getUserID(), image);
                } else {
                    plant.setPlantName(stringName);
                    plant.setReadingNames(stringName);
                    plant.setPlantType(stringGenus);
                    plant.setImage(image);
                    plant.setPotNumber(intPotNumber);
                    viewModelMyPlants.updatePlant(activity, this, plant);
                }

                progressBar.setVisibility(View.VISIBLE);
            } else Toast.makeText(activity, "Failure inserting a new plant type.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void plantResponse(boolean success) {
        progressBar.setVisibility(View.GONE);
        if (success) {
            Toast.makeText(activity, "Successfully added plant.", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
        } else Toast.makeText(activity, "Unable to add plant.", Toast.LENGTH_SHORT).show();
    }

    private boolean validInput() {
        if (plantName.getText().toString().equals("") ||
                plantType.getText().toString().equals("") ||
                potNumber.getText().toString().equals("") ||
                plantTemperature.getText().toString().equals("") ||
                waterSchedule.getText().toString().equals("") ||
                plantLight.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill in all forms.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (potNumberError.getVisibility() == View.VISIBLE ||
                !viewModelNewPlant.checkPotNumberExists(Integer.parseInt(potNumber.getText().toString()))) {
            Toast.makeText(activity, "Please give a valid SmartPot number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }

    private void camera_onClick(View view) {
         AlertDialog dialog = new AlertDialog.Builder(activity)
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
                    camera.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_camera));
                    dialogInterface.dismiss();
                })
                 .create();
         dialog.show();
    }

    private void populateFields() {
        plantName.setText(plant.getPlantName());
        plantType.setText(plant.getPlantType());
        potNumber.setText(String.valueOf(plant.getPotNumber()));

        Bitmap bitmap = Base64Tool.decodeImage(plant.getImage());
        camera.setImageBitmap(bitmap);
        profilePicture = bitmap;
    }

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

                    waterSchedule.setEnabled(true);
                    waterSchedule.setText("");

                    plantLight.setEnabled(true);
                    plantLight.setText("");
                } else {
                    plantTemperature.setEnabled(false);
                    waterSchedule.setEnabled(false);
                    plantLight.setEnabled(false);
                }
            } else {
                newTypeMessage.setVisibility(View.GONE);

                plantTemperature.setEnabled(false);
                plantTemperature.setText(String.valueOf(parameter.getTemp()));

                waterSchedule.setEnabled(false);
                waterSchedule.setText(String.valueOf(parameter.getNextWaterHour()));

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