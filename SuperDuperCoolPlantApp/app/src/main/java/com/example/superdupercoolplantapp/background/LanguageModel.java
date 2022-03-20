package com.example.superdupercoolplantapp.background;

import com.example.superdupercoolplantapp.background.viewmodels.CommonStorage;

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

                if (emotion == Emotion.THIRSTY)
                    comments.append(plantName).append(" is thirsty.");
                else if (emotion == Emotion.HUMID)
                    comments.append(plantName).append(" has too much water.");
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

                if (emotion == Emotion.THIRSTY)
                    comments.append("We added some water.");
                else if (emotion == Emotion.HUMID)
                    comments.append("We will slow down with the water.");
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
        else if (emotion == Emotion.THIRSTY) return "I need more water!";
        else if (emotion == Emotion.HUMID) return "I'm getting too much water!";
        else if (emotion == Emotion.COLD) return "I'm too cold!";
        else if (emotion == Emotion.HOT) return "It's too hot over here!";
        else if (emotion == Emotion.DARK) return "I'm not getting enough light!";
        else if (emotion == Emotion.LIGHT) return "I'm getting too much light!";
        else return null;
    }

    public static String botResponseEngine(Emotion emotion, String plantName) {
        if (emotion == Emotion.HAPPY) return "That's great to hear! I'll try to maintain the current conditions!";
        else if (emotion == Emotion.THIRSTY) return "I'll add some water!";
        else if (emotion == Emotion.HUMID) return "I'll slow down with the water!";
        else if (emotion == Emotion.COLD) return
                MessageFormat.format("Hey, {0}! can you please move {1} to somewhere warmer?",
                        CommonStorage.INSTANCE.getUserRealName(), plantName);
        else if (emotion == Emotion.HOT) return
                MessageFormat.format("Hey, {0}! can you please move {1} to somewhere cooler?",
                        CommonStorage.INSTANCE.getUserRealName(), plantName);
        else if (emotion == Emotion.DARK) return "I'll make the lights brighter!";
        else if (emotion == Emotion.LIGHT) return "I'll dim the lights!";
        return null;
    }
}
