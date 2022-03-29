package com.example.superdupercoolplantapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.fragments.MyPlantsDirections;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MyPlantsAdapter extends RecyclerView.Adapter<MyPlantsAdapter.ViewHolder> {
    private final NavController navController;
    private ArrayList<Plant> plants;
    private ArrayList<Plant> filteredPlants;

    public MyPlantsAdapter(NavController navController) {
        this.navController = navController;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Plant> plants) {
        this.plants = plants;
        this.filteredPlants = plants;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String filter) {
        if (!filter.equals("")) {
            filteredPlants = plants.stream()
                    .filter(i -> i.getPlantName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {    // clear search
            filteredPlants = plants;
        }
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
        Plant plant = filteredPlants.get(position);

        if (position == 0)
            holder.divider.setVisibility(View.GONE);

        holder.plantName.setText(plant.getPlantName());
        holder.plantType.setText(plant.getPlantType());
        holder.emoji.setText(plant.getEmotion().getEmoji());
        holder.profilePic.setImageBitmap(Base64Tool.decodeImage(plant.getImage()));

        holder.layout.setOnClickListener(view -> {
            MyPlantsDirections.ActionMyPlantsToPlantDetail action =
                    MyPlantsDirections.actionMyPlantsToPlantDetail(plant.getPlantID());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        if (filteredPlants != null) return filteredPlants.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View divider;
        private final TextView plantName, plantType, emoji;
        private final ConstraintLayout layout;
        private final ImageView profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.rec_view_my_plants_divider);
            plantName = itemView.findViewById(R.id.rec_view_my_plants_name);
            plantType = itemView.findViewById(R.id.rec_view_my_plants_type);
            emoji = itemView.findViewById(R.id.rec_view_my_plants_emoji);
            layout = itemView.findViewById(R.id.rec_view_my_plants_layout);
            profilePic = itemView.findViewById(R.id.rec_view_my_plants_image);
        }
    }
}
