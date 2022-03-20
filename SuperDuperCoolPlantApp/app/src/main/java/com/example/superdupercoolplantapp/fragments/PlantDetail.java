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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.LanguageModel;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelMyPlants;

public class PlantDetail extends Fragment {
    private ImageView profilePicture;
    private TextView type, potNumber, lastScan, emojis, nextScan, scanResults, lastAction;
    private Button expand;

    private MainActivity activity;
    private ViewModelMyPlants viewModel;
    private NavController navController;
    private Plant plant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        activity = (MainActivity) requireActivity();

        profilePicture = view.findViewById(R.id.plant_detail_image);
        type = view.findViewById(R.id.plant_detail_type);
        potNumber = view.findViewById(R.id.plant_detail_pot_number);
        lastScan = view.findViewById(R.id.plant_detail_last_scan);
        emojis = view.findViewById(R.id.plant_detail_emoji);
        nextScan = view.findViewById(R.id.plant_detail_next_scan);
        scanResults = view.findViewById(R.id.plant_detail_scan_results);
        lastAction = view.findViewById(R.id.plant_detail_scan_action);
        expand = view.findViewById(R.id.plant_detail_expand);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(activity).get(ViewModelMyPlants.class);

        int plantID = PlantDetailArgs.fromBundle(getArguments()).getIdPlant();
        plant = viewModel.getPlantByID(plantID);

        if (plant == null) {
            Toast.makeText(activity, "Unable to find plant details.", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
        } else {
            populateFields();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void populateFields() {
        activity.hideBottomNav();
        activity.setText(plant.getPlantName());

        profilePicture.setImageBitmap(Base64Tool.decodeImage(plant.getImage()));
        type.setText(plant.getPlantType());
        potNumber.setText(String.valueOf(plant.getPotNumber()));

        Reading mostRecent = plant.getMostRecentReading();

        if (mostRecent != null) {
            lastScan.setText(Utilities.getHowLongAgo(mostRecent.getTimestamp()));
            emojis.setText(LanguageModel.listEmojis(mostRecent.getEmotions()));
            scanResults.setText(LanguageModel.scanResult(plant.getPlantName(), mostRecent.getEmotions()));
            lastAction.setText(LanguageModel.actionResult(mostRecent.getEmotions()));
        } else {
            lastScan.setText("N/A");
            emojis.setText("N/A");
            scanResults.setText("N/A");
            lastAction.setText("N/A");
        }

        nextScan.setText((plant.getNextScan() != null)?
                Utilities.getInHowLong(plant.getNextScan().getTimestamp())
                : "N/A");
    }
}