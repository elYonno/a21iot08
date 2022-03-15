package com.example.superdupercoolplantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.models.Plant;

import java.util.ArrayList;

public class MyPlantsAdapter extends RecyclerView.Adapter<MyPlantsAdapter.ViewHolder> {
    private NavController navController;
    private ArrayList<Plant> plants;

    public MyPlantsAdapter(NavController navController) {
        this.navController = navController;
    }

    public void update(ArrayList<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_my_plants, parent,false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant plant = plants.get(position);

        String title = String.format("%s %s", plant.getEmotion().getEmoji(), plant.getPlantName());
        holder.plantName.setText(title);
        holder.plantType.setText(plant.getPlantType());

        holder.layout.setOnClickListener(view -> {
            // TODO navigate to plant page
        });
    }

    @Override
    public int getItemCount() {
        if (plants != null) return plants.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView plantName, plantType;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.rec_view_my_plants_name);
            plantType = itemView.findViewById(R.id.rec_view_my_plants_type);
            layout = itemView.findViewById(R.id.rec_view_my_plants_layout);
        }
    }
}
