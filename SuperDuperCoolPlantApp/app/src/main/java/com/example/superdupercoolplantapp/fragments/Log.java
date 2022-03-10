package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;

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

        future = view.findViewById(R.id.log_future_rec);
        future.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //TODO: create adapters

        past = view.findViewById(R.id.log_past_rec);
        past.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.log));
        activity.showBottomNav();
    }
}