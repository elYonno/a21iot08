package com.example.superdupercoolplantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.R;

public class MyPlantsAdapter extends RecyclerView.Adapter<MyPlantsAdapter.ViewHolder> {
    private final View view;
    private NavController navController;

    public MyPlantsAdapter(View view) {
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        navController = Navigation.findNavController(view);
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_my_plants, parent,false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView plantName, plantType;
        private ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.rec_view_my_plants_name);
            plantType = itemView.findViewById(R.id.rec_view_my_plants_type);
            layout = itemView.findViewById(R.id.rec_view_my_plants_layout);
        }
    }
}
