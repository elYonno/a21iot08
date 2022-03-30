package com.example.superdupercoolplantapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.LanguageModel;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.databasefunctions.Readings;
import com.example.superdupercoolplantapp.background.databasefunctions.Schedule;
import com.example.superdupercoolplantapp.background.interfaces.PlantInterface;
import com.example.superdupercoolplantapp.background.interfaces.ReadingsObserver;
import com.example.superdupercoolplantapp.background.interfaces.RefreshOverride;
import com.example.superdupercoolplantapp.background.interfaces.ScheduleObserver;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;

public class PlantDetail extends Fragment implements PlantInterface, RefreshOverride, ReadingsObserver, ScheduleObserver {
    private ImageView profilePicture;
    private TextView type, potNumber, lastScan, emojis, nextScan, scanResults, lastAction;
    private MotionLayout motionLayout;

    private MainActivity activity;
    private ViewModelMyPlants viewModel;
    private NavController navController;
    private Plant plant;

    private boolean seeButtons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        activity = (MainActivity) requireActivity();
        seeButtons = false;

        motionLayout = view.findViewById(R.id.plant_detail_motion_layout);

        profilePicture = view.findViewById(R.id.plant_detail_image);
        type = view.findViewById(R.id.plant_detail_type);
        potNumber = view.findViewById(R.id.plant_detail_pot_number);
        lastScan = view.findViewById(R.id.plant_detail_last_scan);
        emojis = view.findViewById(R.id.plant_detail_emoji);
        nextScan = view.findViewById(R.id.plant_detail_next_scan);
        scanResults = view.findViewById(R.id.plant_detail_scan_results);
        lastAction = view.findViewById(R.id.plant_detail_scan_action);

        Button expand = view.findViewById(R.id.plant_detail_expand);
        expand.setOnClickListener(this::onExpandClicked);

        Button delete = view.findViewById(R.id.plant_detail_delete);
        delete.setOnClickListener(this::onDeleteClicked);

        ScrollView scrollView = view.findViewById(R.id.plant_detail_scroll);
        scrollView.setOnScrollChangeListener((view1, x, y, oldX, oldY) -> {
            if (y > oldY) motionLayout.transitionToState(R.id.transition_scroll_down);
            else motionLayout.transitionToStart();
        });

        Button edit = view.findViewById(R.id.plant_detail_edit);
        edit.setOnClickListener(this::onEditClicked);

        return view;
    }

    private void onEditClicked(View view) {
        PlantDetailDirections.ActionPlantDetailToNewPlant action =
                PlantDetailDirections.actionPlantDetailToNewPlant(plant.getPlantID());
        navController.navigate(action);
    }

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

    @Override
    public void onStart() {
        super.onStart();

        activity.hideBottomNav();
        activity.setRefreshOverride(this);
        Readings.INSTANCE.addObserver(this);
        Schedule.INSTANCE.addObserver(this);
    }

    private void onExpandClicked(View view) {
        if (seeButtons) {
            seeButtons = false;
            motionLayout.transitionToStart();
        } else {
            seeButtons = true;
            motionLayout.transitionToEnd();
        }
    }

    private void onDeleteClicked(View view) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Delete Plant")
                .setMessage("Are you sure you want to delete this plant?")
                .setPositiveButton("Yes",
                        (dialogInterface, i) -> viewModel.deletePlant(activity, plant, this))
                .setNeutralButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        dialog.show();
    }

    private void populateFields() {
        activity.setText(plant.getPlantName());

        profilePicture.setImageBitmap(Base64Tool.decodeImage(plant.getImage()));
        type.setText(plant.getPlantType());
        potNumber.setText(String.valueOf(plant.getPotNumber()));
    }

    @Override
    public void plantResponse(boolean success) {
        // delete response
        if (success) {
            Toast.makeText(activity, "Successfully deleted plant.", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
        } else {
            Toast.makeText(activity, "Unable to delete plant.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshResponse() {
        if (plant != null) {
            Readings.INSTANCE.queryRecentReadings(activity, plant, true);
            Schedule.INSTANCE.getNextScans(activity, plant, true);
        } else {
            Toast.makeText(activity, "Unable to find plant details.", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
            activity.stopRefreshAnimation();
        }
    }

    @Override
    public void updateReadings() {
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
        activity.stopRefreshAnimation();
    }

    @Override
    public void updateSchedule() {
        nextScan.setText((plant.getNextScan() != null)?
                Utilities.getInHowLong(plant.getNextScan().getTimestamp())
                : "N/A");
        activity.stopRefreshAnimation();
    }
}