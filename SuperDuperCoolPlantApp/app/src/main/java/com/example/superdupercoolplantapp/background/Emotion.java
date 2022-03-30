package com.example.superdupercoolplantapp.background;

public enum Emotion {
    UNKNOWN("❓"),
    HAPPY("\uD83D\uDE03"),
    ANGRY("\uD83D\uDE21"),
    THIRSTY("\uD83E\uDD24"),
    HUMID("\uD83C\uDF0A"),
    COLD("\uD83E\uDD76"),
    HOT("\uD83E\uDD75"),
    DARK("\uD83C\uDF1A"),
    LIGHT("\uD83C\uDF1E");

    String s;

    Emotion(String s) {
        this.s = s;
    }

    public String getEmoji() {
        return s;
    }
}
