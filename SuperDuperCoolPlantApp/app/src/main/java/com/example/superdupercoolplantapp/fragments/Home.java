package com.example.superdupercoolplantapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.ChatAdapter;
import com.example.superdupercoolplantapp.background.databasefunctions.Readings;
import com.example.superdupercoolplantapp.background.interfaces.ReadingsObserver;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;

import java.util.ArrayList;

public class Home extends Fragment implements ReadingsObserver {
    private MainActivity activity;
    private ArrayList<Plant> plants;
    private ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = (MainActivity) requireActivity();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.home_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new ChatAdapter(activity);
        recyclerView.setAdapter(adapter);
        ViewModelMyPlants viewModelMyPlants = new ViewModelProvider(activity).get(ViewModelMyPlants.class);
        viewModelMyPlants.getPlants().observe(activity, this::onPlantsChange);
    }

    private void onPlantsChange(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.home));
        activity.showBottomNav();
        activity.setRefreshEnabled(true);

        Readings.INSTANCE.addObserver(this);
    }

    @Override
    public void updateReadings() {
        if (plants != null)
            adapter.setPlants(plants);
    }
}