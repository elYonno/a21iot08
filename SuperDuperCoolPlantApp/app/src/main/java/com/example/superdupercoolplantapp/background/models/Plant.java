package com.example.superdupercoolplantapp.background.models;

import com.example.superdupercoolplantapp.background.Emotion;

import java.util.ArrayList;

public class Plant {
    private final int plantID;
    private int potNumber;
    private String plantName, plantType, image;
    private final ArrayList<Reading> recentReadings;
    private NextScan nextScan;
    private Emotion emotion;

    public Plant(int plantID, int potNumber, String plantName, String plantType, String image) {
        this.plantID = plantID;
        this.potNumber = potNumber;
        this.plantName = plantName;
        this.plantType = plantType;
        this.image = image;
        this.recentReadings = new ArrayList<>();
        this.nextScan = null;
        this.emotion = Emotion.HAPPY;
    }

    public int getPlantID() {
        return plantID;
    }

    public int getPotNumber() {
        return potNumber;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getPlantType() {
        return plantType;
    }

    public String getImage() {
        return image;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public ArrayList<Reading> getRecentReadings() {
        return recentReadings;
    }

    public void setPotNumber(int potNumber) {
        this.potNumber = potNumber;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addReading(Reading reading) {
        recentReadings.add(reading);

        if (recentReadings.size() == 1) { // most recent reading
            if (reading.getEmotions().size() == 0) emotion = Emotion.HAPPY;                     // no problem
            else if (reading.getEmotions().size() == 1) emotion = reading.getEmotions().get(0); // has 1 problem
            else emotion = Emotion.ANGRY;                                                       // multiple problems
        }
    }

    public Reading getMostRecentReading() {
        if (recentReadings.size() != 0) return recentReadings.get(0);
        else return null;
    }

    public NextScan getNextScan() {
        return nextScan;
    }

    public void setNextScan(NextScan nextScan) {
        this.nextScan = nextScan;
    }
}
