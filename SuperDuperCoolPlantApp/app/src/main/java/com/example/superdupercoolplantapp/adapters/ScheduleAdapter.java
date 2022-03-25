package com.example.superdupercoolplantapp.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.NextScan;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.fragments.LogDirections;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<NextScan> nextScans;
    private final NavController navController;

    public ScheduleAdapter(NavController navController) {
        this.navController = navController;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Plant> plants) {
        this.nextScans = plants
                .stream()
                .map(Plant::getNextScan)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_schedule, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NextScan nextScan = nextScans.get(position);

        holder.plantName.setText(nextScan.getPlantName());
        holder.time.setText(Utilities.getInHowLong(nextScan.getTimestamp()));

        holder.card.setOnClickListener(view -> {
            LogDirections.ActionLogToPlantDetail action =
                    LogDirections.actionLogToPlantDetail(nextScan.getPlantID());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        if (nextScans != null) return nextScans.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView plantName, time;
        private final CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.rec_view_schedule_name);
            time = itemView.findViewById(R.id.rec_view_schedule_time);
            card = itemView.findViewById(R.id.rec_view_schedule_card);
        }
    }
}
