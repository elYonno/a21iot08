package com.example.superdupercoolplantapp.background.models;

import androidx.annotation.NonNull;

public class Parameter {
    private final int plantType, nextWaterHour;
    private final String plantGenus;
    private final double light, temp;

    public Parameter(int plantType, @NonNull String plantGenus, double light, int nextWaterHour, double temp) {
        this.plantType = plantType;
        this.plantGenus = plantGenus;
        this.light = light;
        this.nextWaterHour = nextWaterHour;
        this.temp = temp;
    }

    public int getPlantType() {
        return plantType;
    }

    public String getPlantGenus() {
        return plantGenus;
    }

    public double getLight() {
        return light;
    }

    public int getNextWaterHour() {
        return nextWaterHour;
    }

    public double getTemp() {
        return temp;
    }
}
