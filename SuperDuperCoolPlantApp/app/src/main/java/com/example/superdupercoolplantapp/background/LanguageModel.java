package com.example.superdupercoolplantapp.background;

import java.util.ArrayList;

public class LanguageModel {
    public static String getComments(String plantName, ArrayList<Emotion> emotions) {
        StringBuilder comments = new StringBuilder();
        int count = 0;

        if (emotions.size() != 0)
            for (Emotion emotion : emotions) {
                if (count != 0) comments.append("\n");

                if (emotion == Emotion.THIRSTY)
                    comments.append(Emotion.THIRSTY.getEmoji()).append("\t").append(plantName).append(" is thirsty. We added some water.");
                else if (emotion == Emotion.HUMID)
                    comments.append(Emotion.HUMID.getEmoji()).append("\t").append(plantName).append(" has too much water. We will slow down with the water.");
                else if (emotion == Emotion.COLD)
                    comments.append(Emotion.COLD.getEmoji()).append("\t").append(plantName).append(" is cold. We will turn up the lights.");
                else if (emotion == Emotion.HOT)
                    comments.append(Emotion.HOT.getEmoji()).append("\t").append(plantName).append(" is hot. We will dim the lights.");
                else if (emotion == Emotion.DARK)
                    comments.append(Emotion.DARK.getEmoji()).append("\t").append(plantName).append(" is not getting enough light. We will turn up the lights.");
                else if (emotion == Emotion.LIGHT)
                    comments.append(Emotion.LIGHT.getEmoji()).append("\t").append(plantName).append(" is getting too much light. We will dim the lights.");
                count++;
            }
        else {
            comments.append(Emotion.HAPPY.getEmoji()).append("\t").append(plantName).append(" is vibing. Maintaining parameters!");
        }

        return comments.toString();
    }
}
