package com.example.superdupercoolplantapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.LanguageModel;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.fragments.LogDirections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ReadingsAdapter extends RecyclerView.Adapter<ReadingsAdapter.ViewHolder> {
    private ArrayList<Reading> readings;
    private final NavController navController;
    private final RecyclerView rec;

    public ReadingsAdapter(NavController navController, RecyclerView rec) {
        this.navController = navController;
        this.rec = rec;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Plant> plants) {
        this.readings = plants.stream()
                .flatMap(Plant::getRecentReadingsStream)
                .sorted(Comparator.comparing(Reading::getTimestamp).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_scans, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reading reading = readings.get(position);
        ArrayList<Emotion> emotions = reading.getEmotions();

        holder.name.setText(String.format("%s\t%s",
                reading.getPlantName(), LanguageModel.listEmojis(emotions)));

        holder.time.setText(Utilities.getHowLongAgo(reading.getTimestamp()));

        holder.comment.setText(String.format("%s\n\n%s\n\n%s",
                LanguageModel.scanResult(reading.getPlantName(), reading.getEmotions()),
                LanguageModel.actionResult(reading.getEmotions()),
                Utilities.getFormattedReadings(reading)));

        if (position == 0) holder.divider.setVisibility(View.GONE);

        holder.name.setOnClickListener(view -> {
            LogDirections.ActionLogToPlantDetail action =
                    LogDirections.actionLogToPlantDetail(reading.getPlantID());
            navController.navigate(action);
        });


        holder.layout.setOnClickListener(view -> {
            if (holder.comment.getVisibility() == View.VISIBLE) {
                holder.layout.transitionToStart();
                holder.time.setText(Utilities.getHowLongAgo(reading.getTimestamp()));
            } else {
                holder.layout.transitionToEnd();
                holder.time.setText(Utilities.getFormattedTime(reading.getTimestamp()));
                hideViews(position);
            }
        });

        holder.layout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.scans_start) {
                    holder.time.setText(Utilities.getHowLongAgo(reading.getTimestamp()));
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }

    private void hideViews(int i) {
        for (int j = 0; j < getItemCount(); j++) {
            if (j != i) {
                ReadingsAdapter.ViewHolder view = (ViewHolder) rec.findViewHolderForAdapterPosition(j);
                if (view != null) {
                    view.hide();
                }
            }
        }
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

        public void hide() {
            layout.transitionToStart();
        }
    }
}
