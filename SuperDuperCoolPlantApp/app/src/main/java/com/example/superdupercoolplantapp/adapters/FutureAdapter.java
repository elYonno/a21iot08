package com.example.superdupercoolplantapp.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.NextScan;

import java.util.ArrayList;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.ViewHolder> {
    private ArrayList<NextScan> nextScans;
    private final NavController navController;

    public FutureAdapter(NavController navController) {
        this.navController = navController;
    }

    public void update(ArrayList<NextScan> nextScans) {
        this.nextScans = nextScans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_future, parent, false);
        return new ViewHolder(card);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NextScan nextScan = nextScans.get(position);

        holder.plantName.setText(nextScan.getPlantName());
        holder.time.setText(Utilities.getInHowLong(nextScan.getNextScan()));

        holder.card.setOnClickListener(view -> {
            // TODO go to plant
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
