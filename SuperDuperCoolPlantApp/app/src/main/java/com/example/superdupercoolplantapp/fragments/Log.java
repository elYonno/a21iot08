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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.ScheduleAdapter;
import com.example.superdupercoolplantapp.adapters.ScansAdapter;
import com.example.superdupercoolplantapp.background.models.NextScan;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelNextScans;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelReadings;

import java.util.ArrayList;

public class Log extends Fragment {
    private MainActivity activity;

    private ScansAdapter scansAdapter;
    private ScheduleAdapter scheduleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();

        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        scansAdapter = new ScansAdapter(navController);
        past.setAdapter(scansAdapter);

        ViewModelReadings readings = new ViewModelProvider(activity).get(ViewModelReadings.class);
        readings.getRecentReadings().observe(activity, this::updatePast);

        ViewModelNextScans nextScans = new ViewModelProvider(activity).get(ViewModelNextScans.class);
        nextScans.getNextScans().observe(activity, this::updateFuture);
    }

    private void updateFuture(ArrayList<NextScan> nextScans) {
        scheduleAdapter.update(nextScans);
    }

    private void updatePast(ArrayList<Reading> readings) {
        scansAdapter.update(readings);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.log));
        activity.showBottomNav();
    }
}