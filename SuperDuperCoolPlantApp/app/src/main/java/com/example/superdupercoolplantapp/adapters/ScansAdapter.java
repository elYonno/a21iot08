package com.example.superdupercoolplantapp.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.LanguageModel;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.fragments.LogDirections;

import java.util.ArrayList;

public class ScansAdapter extends RecyclerView.Adapter<ScansAdapter.ViewHolder> {
    private ArrayList<Reading> readings;
    private final NavController navController;

    public ScansAdapter(NavController navController) {
        this.navController = navController;
    }

    public void update(ArrayList<Reading> readings) {
        this.readings = readings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_scans, parent, false);
        return new ViewHolder(card);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reading reading = readings.get(position);
        ArrayList<Emotion> emotions = reading.getEmotions();

        holder.name.setText(String.format("%s\t%s",
                reading.getPlantName(), LanguageModel.listEmojis(emotions)));

        holder.time.setText(Utilities.getHowLongAgo(reading.getTimestamp()));

        holder.comment.setText(String.format("%s\n\n%s",
                LanguageModel.scanResult(reading.getPlantName(), reading.getEmotions()),
                LanguageModel.actionResult(reading.getEmotions())));

        if (position == 0) holder.divider.setVisibility(View.GONE);

        holder.name.setOnClickListener(view -> {
            LogDirections.ActionLogToPlantDetail action =
                    LogDirections.actionLogToPlantDetail(reading.getPlantID());
            navController.navigate(action);
        });


        holder.layout.setOnClickListener(view -> {
            if (holder.comment.getVisibility() == View.VISIBLE) {
                holder.layout.transitionToStart();
            } else {
                holder.layout.transitionToEnd();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (readings != null) return readings.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, time, comment;
        private final View divider;
        private final MotionLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_view_scan_name);
            time = itemView.findViewById(R.id.rec_view_scan_time);
            comment = itemView.findViewById(R.id.rec_view_scan_comment);
            layout = itemView.findViewById(R.id.rec_view_scan_layout);
            divider = itemView.findViewById(R.id.rec_view_scan_divider);
        }
    }
}
