package com.example.superdupercoolplantapp.background.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.Threshold;
import com.example.superdupercoolplantapp.background.Utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reading {
    private final int plantID;
    private final String plantName;
    private final double lightValue, humidityValue, tempValue;
    private final LocalDateTime timestamp;
    private ArrayList<Emotion> emotions;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Reading(int plantID, String plantName, String rawTimestamp,
                   double lightValue, double humidityValue, double tempValue) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.lightValue = lightValue;
        this.humidityValue = humidityValue;
        this.tempValue = tempValue;
        this.timestamp = Utilities.stringToTimestamp(rawTimestamp);
    }

    public void setEmotions(double deltaLight, double deltaHumidity, double deltaTemp) {
        emotions = new ArrayList<>();

        if (Threshold.LIGHT > deltaLight) emotions.add(Emotion.LIGHT); // too light
        else if (-Threshold.LIGHT > deltaLight) emotions.add(Emotion.DARK); // too dark

        if (Threshold.HUMIDITY > deltaHumidity) emotions.add(Emotion.HUMID); // too humid
        else if (-Threshold.HUMIDITY > deltaHumidity) emotions.add(Emotion.THIRSTY); // too dry

        if (Threshold.TEMPERATURE > deltaHumidity) emotions.add(Emotion.HOT); // too hot
        else if (-Threshold.TEMPERATURE > deltaHumidity) emotions.add(Emotion.COLD); // too cold

        if (emotions.size() == 0) emotions.add(Emotion.HAPPY); // all good
        else if (emotions.size() > 1) emotions.add(Emotion.ANGRY); // multiple problems
    }

    public int getPlantID() {
        return plantID;
    }

    public String getPlantName() {
        return plantName;
    }

    public double getLightValue() {
        return lightValue;
    }

    public double getHumidityValue() {
        return humidityValue;
    }

    public double getTempValue() {
        return tempValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
