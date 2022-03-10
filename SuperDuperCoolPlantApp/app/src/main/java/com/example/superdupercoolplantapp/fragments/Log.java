package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.PastAdapter;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelRecentReadings;

public class Log extends Fragment {
    private MainActivity activity;

    private RecyclerView future;
    private RecyclerView past;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();

        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: create adapters
        future = view.findViewById(R.id.log_future_rec);
        future.setLayoutManager(new LinearLayoutManager(this.getContext()));

        past = view.findViewById(R.id.log_past_rec);
        past.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ViewModelRecentReadings readings = new ViewModelProvider(activity).get(ViewModelRecentReadings.class);
        PastAdapter pastAdapter = new PastAdapter(activity, view, readings);
        past.setAdapter(pastAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.log));
        activity.showBottomNav();
    }
}