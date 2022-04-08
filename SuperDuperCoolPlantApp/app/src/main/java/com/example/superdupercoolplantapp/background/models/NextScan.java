package com.example.superdupercoolplantapp.background.models;

import com.example.superdupercoolplantapp.background.Utilities;

import java.time.LocalDateTime;

public class NextScan {
    private final int plantID;
    private final String plantName;
    private final LocalDateTime schedule, nextWater;

    public NextScan(int plantID, String plantName, String schedule, String nextWater) {
        this.plantID = plantID;
        this.plantName = plantName;

        LocalDateTime newSchedule = Utilities.stringToLocalDateTime(schedule);
        if (newSchedule.isAfter(LocalDateTime.now()))
            this.schedule = newSchedule;
        else
            this.schedule = null;

        LocalDateTime newWater = Utilities.stringToLocalDateTime(nextWater);
        if (newWater.isAfter(LocalDateTime.now()))
            this.nextWater = newSchedule;
        else
            this.nextWater = null;
    }

    public int getPlantID() {
        return plantID;
    }

    public String getPlantName() {
        return plantName;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public LocalDateTime getNextWater() {
        return nextWater;
    }
}
