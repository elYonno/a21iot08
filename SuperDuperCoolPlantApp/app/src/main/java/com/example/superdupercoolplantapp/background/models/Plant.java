package com.example.superdupercoolplantapp.background.models;

import java.util.ArrayList;

public class Plant {
    private int plantID, potNumber;
    private String plantName, plantType, image;
    private NextScan nextScan;
    private ArrayList<Reading> readings;

    public Plant(int plantID, int potNumber, String plantName, String plantType, String image) {
        this.plantID = plantID;
        this.potNumber = potNumber;
        this.plantName = plantName;
        this.plantType = plantType;
        this.image = image;
    }
}
