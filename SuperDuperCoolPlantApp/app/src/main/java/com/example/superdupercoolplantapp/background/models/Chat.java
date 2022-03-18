package com.example.superdupercoolplantapp.background.models;

import androidx.annotation.Nullable;

import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.LanguageModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Chat {
    private final String message;
    private final Plant plant;
    private final Reading reading;

    /**
     * if plant is null, bot is asking
     */
    public Chat(String message, @Nullable Plant plant, Reading reading) {
        this.message = message;
        this.plant = plant;
        this.reading = reading;
    }

    public String getMessage() {
        return message;
    }

    public Plant getPlant() {
        return plant;
    }

    public Reading getReading() {
        return reading;
    }

    public static ArrayList<Chat> createChats(Plant plant, Reading reading) {
        ArrayList<Chat> chats = new ArrayList<>();
        ArrayList<Emotion> emotions = reading.getEmotions();

        // bot asks how the plant is doing
        chats.add(new Chat(MessageFormat.format("Hey {0}! How are you doing?",
                plant.getPlantName()), null, reading));

        for (Emotion emotion : emotions) {
            String plantMessage = LanguageModel.plantChatEngine(emotion);

            if (plantMessage != null) {
                chats.add(new Chat(plantMessage, plant, reading));

                String botResponse = LanguageModel.botResponseEngine(emotion, plant.getPlantName());
                if (botResponse != null) chats.add(new Chat(botResponse, null, reading));
            }
        }

        return chats;
    }
}