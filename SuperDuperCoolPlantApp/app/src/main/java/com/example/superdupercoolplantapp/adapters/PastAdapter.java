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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelRecentReadings;

import java.util.ArrayList;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.ViewHolder> {
    private ArrayList<Reading> readings;
    private final View view;
    private NavController navController;

    public PastAdapter(MainActivity mainActivity, View view, ViewModelRecentReadings viewModel) {
        this.view = view;
        viewModel.getReadings().observe(mainActivity, this::onReadingChange);
    }

    private void onReadingChange(ArrayList<Reading> readings) {
        this.readings = readings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        navController = Navigation.findNavController(view);
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_past, parent, false);
        return new ViewHolder(card);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reading reading = readings.get(position);

        holder.name.setText(reading.getPlantName());
        holder.time.setText(Utilities.getMinutesAgo(reading.getTimestamp()));

        // TODO work out emotion in SQL? then comment from emotion


    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, time, comment;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_view_past_name);
            time = itemView.findViewById(R.id.rec_view_past_time);
            comment = itemView.findViewById(R.id.rec_view_past_comment);
            cardView = itemView.findViewById(R.id.rec_view_past_card);
        }
    }
}
