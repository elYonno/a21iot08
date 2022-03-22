package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.adapters.MyPlantsAdapter;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.databasefunctions.ViewModelMyPlants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyPlants extends Fragment {
    private MainActivity activity;
    private NavController navController;

    private MyPlantsAdapter adapter;

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
        navController = Navigation.findNavController(view);

        EditText search = view.findViewById(R.id.my_plants_search);
        search.addTextChangedListener(new SearchTextWatcher());

        FloatingActionButton btnAdd = view.findViewById(R.id.my_plants_add);
        btnAdd.setOnClickListener(this::btnAdd_onClick);

        RecyclerView recyclerView = view.findViewById(R.id.my_plants_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new MyPlantsAdapter(navController);
        recyclerView.setAdapter(adapter);

        ViewModelMyPlants viewModel = new ViewModelProvider(activity).get(ViewModelMyPlants.class);
        viewModel.getPlants().observe(activity, this::onPlantUpdate);
    }

    private void onPlantUpdate(ArrayList<Plant> plants) {
        adapter.update(plants);
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

    private static class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}