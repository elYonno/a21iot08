package com.example.superdupercoolplantapp.adapters;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Chat;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Chat> chats;
    private final MainActivity activity;

    public ChatAdapter(MainActivity activity) {
        this.activity = activity;
    }

    public void setPlants(ArrayList<Plant> plants) {
        chats = new ArrayList<>();

        for (Plant plant : plants) {
            Reading reading = plant.getMostRecentReading();

            if (reading != null) {
                chats.addAll(Chat.createChats(plant, reading));
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_chat, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        if (chat.getPlant() == null) { // bot message
            holder.plantName.setText(R.string.bot_name);
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_launcher_foreground));
        } else { // plant message
            holder.plantName.setText(chat.getPlant().getPlantName());
            Bitmap profilePic = Base64Tool.decodeImage(chat.getPlant().getImage());
            holder.profilePicture.setImageBitmap(profilePic);
        }

        holder.chat.setText(chat.getMessage());
        holder.time.setText(Utilities.getInHowLong(chat.getReading().getTimestamp()));
    }

    @Override
    public int getItemCount() {
        if (chats != null) return chats.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profilePicture;
        private final TextView chat, plantName, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.rec_view_chat_image);
            chat = itemView.findViewById(R.id.rec_view_chat_text);
            plantName = itemView.findViewById(R.id.rec_view_chat_name);
            time = itemView.findViewById(R.id.rec_view_chat_time);
        }
    }
}
