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
import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.LanguageModel;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.fragments.LogDirections;

import java.util.ArrayList;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.ViewHolder> {
    private ArrayList<Reading> readings;
    private final NavController navController;

    public PastAdapter(NavController navController) {
        this.navController = navController;
    }

    public void update(ArrayList<Reading> readings) {
        this.readings = readings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_past, parent, false);
        return new ViewHolder(card);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reading reading = readings.get(position);
        ArrayList<Emotion> emotions = reading.getEmotions();

        if (emotions.size() > 1) // angry
            holder.name.setText(String.format("%s\t%s", reading.getPlantName(), Emotion.ANGRY.getEmoji()));
        else // everything else
            holder.name.setText(reading.getPlantName());

        holder.time.setText(Utilities.getHowLongAgo(reading.getTimestamp()));

        holder.comment.setText(LanguageModel.logEngine(reading.getPlantName(), reading.getEmotions()));

        holder.cardView.setOnClickListener(view -> {
            LogDirections.ActionLogToPlantDetail action = LogDirections.actionLogToPlantDetail(reading.getPlantID());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        if (readings != null) return readings.size();
        else return 0;
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
