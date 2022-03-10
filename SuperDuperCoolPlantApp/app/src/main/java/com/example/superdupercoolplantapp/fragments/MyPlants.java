package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.MyPlantsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyPlants extends Fragment {
    private MainActivity activity;
    private NavController navController;

    private EditText search;
    private FloatingActionButton btnAdd;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_plants, container, false);
        activity = (MainActivity) requireActivity();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search = view.findViewById(R.id.my_plants_search);

        btnAdd = view.findViewById(R.id.my_plants_add);
        btnAdd.setOnClickListener(this::btnAdd_onClick);

        recyclerView = view.findViewById(R.id.my_plants_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        MyPlantsAdapter adapter = new MyPlantsAdapter(view);
        recyclerView.setAdapter(adapter);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.my_plants));
        activity.showBottomNav();
    }

    private void btnAdd_onClick(View view) {
        MyPlantsDirections.ActionMyPlantsToNewPlant action = MyPlantsDirections.actionMyPlantsToNewPlant(-1);
        navController.navigate(action);
    }
}