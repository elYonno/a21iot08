package com.example.superdupercoolplantapp.background.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.superdupercoolplantapp.background.Utilities;

import java.time.LocalDateTime;

public class NextScan {
    private final int plantID;
    private final String plantName;
    private final LocalDateTime nextScan;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NextScan(int plantID, String plantName, String nextScan) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.nextScan = Utilities.stringToTimestamp(nextScan);
    }

    public int getPlantID() {
        return plantID;
    }


    public String getPlantName() {
        return plantName;
    }

    public LocalDateTime getNextScan() {
        return nextScan;
    }
}
