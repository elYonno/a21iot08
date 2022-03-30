package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.ScheduleAdapter;
import com.example.superdupercoolplantapp.adapters.ReadingsAdapter;
import com.example.superdupercoolplantapp.background.databasefunctions.Schedule;
import com.example.superdupercoolplantapp.background.databasefunctions.Readings;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;
import com.example.superdupercoolplantapp.background.interfaces.ReadingsObserver;
import com.example.superdupercoolplantapp.background.interfaces.ScheduleObserver;
import com.example.superdupercoolplantapp.background.models.Plant;

import java.util.ArrayList;

public class Log extends Fragment implements ReadingsObserver, ScheduleObserver {
    private MainActivity activity;

    private ReadingsAdapter readingsAdapter;
    private ScheduleAdapter scheduleAdapter;

    private ArrayList<Plant> plants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();

        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);

        RecyclerView future = view.findViewById(R.id.log_future_rec);
        future.setLayoutManager(new LinearLayoutManager(this.getContext()));
        scheduleAdapter = new ScheduleAdapter(navController);
        future.setAdapter(scheduleAdapter);

        RecyclerView past = view.findViewById(R.id.log_past_rec);
        past.setLayoutManager(new LinearLayoutManager(this.getContext()));
        readingsAdapter = new ReadingsAdapter(navController, past);
        past.setAdapter(readingsAdapter);

        ViewModelMyPlants viewModel = new ViewModelProvider(activity).get(ViewModelMyPlants.class);
        viewModel.getPlants().observe(activity, this::onPlantsChange);
    }

    private void onPlantsChange(ArrayList<Plant> plants) {
        this.plants = plants;
    }


    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.log));
        activity.showBottomNav();
        activity.setRefreshEnabled(true);

        Readings.INSTANCE.addObserver(this);
        Schedule.INSTANCE.addObserver(this);
    }

    @Override
    public void updateReadings() {
        if (plants != null)
            readingsAdapter.update(plants);
    }


    @Override
    public void updateSchedule() {
        if (plants != null)
            scheduleAdapter.update(plants);
    }
}