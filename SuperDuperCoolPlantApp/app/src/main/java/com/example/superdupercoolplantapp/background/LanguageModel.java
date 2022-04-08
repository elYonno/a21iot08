package com.example.superdupercoolplantapp.background;

import com.example.superdupercoolplantapp.background.databasefunctions.CommonStorage;

import java.text.MessageFormat;
import java.util.ArrayList;

public class LanguageModel {

    public static String listEmojis(ArrayList<Emotion> emotions) {
        StringBuilder emojis = new StringBuilder();

        if (emotions.size() != 0)
            for (Emotion emotion : emotions) {
                emojis.append(" ").append(emotion.getEmoji());
            }
        else emojis.append(Emotion.HAPPY.getEmoji());

        return emojis.toString();
    }

    public static String scanResult(String plantName, ArrayList<Emotion> emotions) {
        StringBuilder comments = new StringBuilder();
        int count = 0;

        if (emotions.size() != 0)
            for (Emotion emotion : emotions) {
                if (count != 0) comments.append("\n");

                if (emotion == Emotion.TANK_EMPTY)
                    comments.append(plantName).append("'s tank is empty.");
                else if (emotion == Emotion.COLD)
                    comments.append(plantName).append(" is cold.");
                else if (emotion == Emotion.HOT)
                    comments.append(plantName).append(" is hot.");
                else if (emotion == Emotion.DARK)
                    comments.append(plantName).append(" is not getting enough light");
                else if (emotion == Emotion.LIGHT)
                    comments.append(plantName).append(" is getting too much light.");
                count++;
            }
        else comments.append(plantName).append(" is vibing.");

        return comments.toString();
    }

    public static String actionResult(ArrayList<Emotion> emotions) {
        StringBuilder comments = new StringBuilder();
        int count = 0;

        if (emotions.size() != 0)
            for (Emotion emotion : emotions) {
                if (count != 0) comments.append("\n");

                if (emotion == Emotion.TANK_EMPTY)
                    comments.append("Please fill up the tank.");
                else if (emotion == Emotion.COLD)
                    comments.append("Please move the plant to a warmer place.");
                else if (emotion == Emotion.HOT)
                    comments.append("Please move the plant to a cooler place.");
                else if (emotion == Emotion.DARK)
                    comments.append("We will turn up the lights.");
                else if (emotion == Emotion.LIGHT)
                    comments.append("We will dim the lights.");
                count++;
            }
        else comments.append("Maintaining parameters.");

        return comments.toString();
    }

    public static String plantChatEngine(Emotion emotion) {
        if (emotion == Emotion.HAPPY) return "I'm doing fine, thanks for asking! \uD83D\uDE0A";
        else if (emotion == Emotion.TANK_EMPTY) return "My water tank ran out!";
        else if (emotion == Emotion.COLD) return "I'm too cold!";
        else if (emotion == Emotion.HOT) return "It's too hot over here!";
        else if (emotion == Emotion.DARK) return "I'm not getting enough light!";
        else if (emotion == Emotion.LIGHT) return "I'm getting too much light!";
        else return null;
    }

    public static String botResponseEngine(Emotion emotion, String plantName) {
        String userName = "<font color='#0099ff'>@" + CommonStorage.INSTANCE.getUserRealName() + "</font>";

        if (emotion == Emotion.HAPPY) return "That's great to hear! I'll try to maintain the current conditions!";
        else if (emotion == Emotion.TANK_EMPTY) return
                MessageFormat.format("Hey, {0}! can you please fill {1}'s water tank?",
                    userName, plantName);
        else if (emotion == Emotion.COLD) return
                MessageFormat.format("Hey, {0}! can you please move {1} to somewhere warmer?",
                        userName, plantName);
        else if (emotion == Emotion.HOT) return
                MessageFormat.format("Hey, {0}! can you please move {1} to somewhere cooler?",
                        userName, plantName);
        else if (emotion == Emotion.DARK) return "I'll make the lights brighter!";
        else if (emotion == Emotion.LIGHT) return "I'll dim the lights!";
        return null;
    }
}
