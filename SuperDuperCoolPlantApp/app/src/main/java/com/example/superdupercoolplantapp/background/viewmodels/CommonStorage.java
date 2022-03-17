package com.example.superdupercoolplantapp.background.viewmodels;

import com.example.superdupercoolplantapp.background.models.Reading;

import java.util.ArrayList;

public enum CommonStorage {
    INSTANCE;

    private String userRealName;

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserRealName() {
        return userRealName;
    }

}
