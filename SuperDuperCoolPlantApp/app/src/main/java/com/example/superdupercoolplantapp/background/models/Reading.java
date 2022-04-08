package com.example.superdupercoolplantapp.background.models;

import com.example.superdupercoolplantapp.background.Emotion;
import com.example.superdupercoolplantapp.background.Threshold;
import com.example.superdupercoolplantapp.background.Utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reading {
    private final int plantID;
    private String plantName;
    private final double lightValue, tempValue;
    private final boolean tankEmpty;
    private final LocalDateTime timestamp;
    private ArrayList<Emotion> emotions;

    public Reading(int plantID, String plantName, String rawTimestamp,
                   double lightValue, boolean tankEmpty, double tempValue) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.lightValue = lightValue;
        this.tankEmpty = tankEmpty;
        this.tempValue = tempValue;
        this.timestamp = Utilities.stringToLocalDateTime(rawTimestamp);
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    /**
     * Creates list of emotions. If list is empty, plant is happy. If list has a size larger than 1,
     * plant is angry as there are multiple problems.
     * @param deltaLight difference in light to optimal
     * @param deltaTemp difference in temperature to optimal
     */
    public void setEmotions(double deltaLight, boolean tankEmpty, double deltaTemp) {
        emotions = new ArrayList<>();

        if (Threshold.LIGHT < deltaLight) emotions.add(Emotion.LIGHT); // too light
        else if (-Threshold.LIGHT > deltaLight) emotions.add(Emotion.DARK); // too dark

        if (tankEmpty) emotions.add(Emotion.TANK_EMPTY);

        if (Threshold.TEMPERATURE < deltaTemp) emotions.add(Emotion.HOT); // too hot
        else if (-Threshold.TEMPERATURE > deltaTemp) emotions.add(Emotion.COLD); // too cold
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

    public boolean isTankEmpty() {
        return tankEmpty;
    }

    public double getTempValue() {
        return tempValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ArrayList<Emotion> getEmotions() {
        return emotions;
    }
}
