package com.example.superdupercoolplantapp.background;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Utilities {

    public static LocalDateTime stringToTimestamp(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(raw, formatter);
    }

    public static String getMinutesAgo(LocalDateTime dateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), dateTime);
        int minutes = (int) Math.abs(duration.getSeconds() / 60);
        if (minutes < 1) return "A few moments ago";
        else if (minutes == 1) return "A minute ago";
        else return minutes + " minutes ago";
    }
}
