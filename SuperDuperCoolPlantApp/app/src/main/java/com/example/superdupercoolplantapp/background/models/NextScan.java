package com.example.superdupercoolplantapp.background.models;

import com.example.superdupercoolplantapp.background.Utilities;

import java.time.LocalDateTime;

public class NextScan {
    private final int plantID;
    private final String plantName;
    private final LocalDateTime timestamp;

    public NextScan(int plantID, String plantName, String timestamp) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.timestamp = Utilities.stringToTimestamp(timestamp);
    }

    public int getPlantID() {
        return plantID;
    }


    public String getPlantName() {
        return plantName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
