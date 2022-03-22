package com.example.superdupercoolplantapp.background.databasefunctions;

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
