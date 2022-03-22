package com.example.superdupercoolplantapp.background.models;

import androidx.annotation.NonNull;

public class Parameter {
    private final int plantType;
    private final String plantGenus;
    private final double light, humidity, temp;

    public Parameter(int plantType, @NonNull String plantGenus, double light, double humidity, double temp) {
        this.plantType = plantType;
        this.plantGenus = plantGenus;
        this.light = light;
        this.humidity = humidity;
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

    public double getHumidity() {
        return humidity;
    }

    public double getTemp() {
        return temp;
    }
}
