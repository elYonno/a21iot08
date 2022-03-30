package com.example.superdupercoolplantapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.Base64Tool;
import com.example.superdupercoolplantapp.background.Utilities;
import com.example.superdupercoolplantapp.background.models.Chat;
import com.example.superdupercoolplantapp.background.models.Plant;
import com.example.superdupercoolplantapp.background.models.Reading;
import com.example.superdupercoolplantapp.fragments.HomeDirections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Chat> chats;
    private final MainActivity activity;

    public ChatAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPlants(ArrayList<Plant> plants) {
        chats = new ArrayList<>();

        for (Plant plant : plants) {
            Reading reading = plant.getMostRecentReading();

            if (reading != null) {
                chats.addAll(Chat.createChats(plant, reading));
            }
        }

        chats.sort(Comparator.comparing(chat -> chat.getReading().getTimestamp()));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        if (chat.getPlant() == null) { // bot message
            holder.plantName.setText(R.string.bot_name);
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(activity, R.mipmap.ic_flora_bot));
        } else { // plant message
            holder.plantName.setText(chat.getPlant().getPlantName());
            holder.plantName.setSelected(true);
            Bitmap profilePic = Base64Tool.decodeImage(chat.getPlant().getImage());
            holder.profilePicture.setImageBitmap(profilePic);

            holder.layout.setOnClickListener(view -> {
                NavController navController = Navigation.findNavController(view);
                HomeDirections.ActionHome2ToPlantDetail action =
                        HomeDirections.actionHome2ToPlantDetail(chat.getPlant().getPlantID());
                navController.navigate(action);
            });
        }

        holder.chat.setText(HtmlCompat.fromHtml(chat.getMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.time.setText(Utilities.getFormattedTime(chat.getReading().getTimestamp()));
    }

    @Override
    public int getItemCount() {
        if (chats != null) return chats.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profilePicture;
        private final TextView chat, plantName, time;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.rec_view_chat_image);
            chat = itemView.findViewById(R.id.rec_view_chat_text);
            plantName = itemView.findViewById(R.id.rec_view_chat_name);
            time = itemView.findViewById(R.id.rec_view_chat_time);
            layout = itemView.findViewById(R.id.rec_view_chat_layout);
        }
    }
}
