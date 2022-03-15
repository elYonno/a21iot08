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

    public static String getHowLongAgo(LocalDateTime dateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), dateTime);
        int minutes = (int) Math.abs(duration.getSeconds() / 60);
        if (minutes < 1) return "A few moments ago";
        else if (minutes == 1) return "A minute ago";
        else if (minutes >= 60) {
            int hours = minutes / 60;
            if (hours == 1) return  "An hour ago";
            else if (hours < 24) return hours + " hours ago";
            else return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        else return minutes + " minutes ago";
    }

    public static String getInHowLong(LocalDateTime dateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), dateTime);
        int minutes = (int) Math.abs(duration.getSeconds() / 60);
        if (minutes < 1) return "In a few moments";
        else if (minutes == 1) return "In a minute";
        else if (minutes >= 60) {
            int hours = minutes / 60;
            if (hours == 1) return  "In one hour";
            else if (hours < 24) return "In" + hours + " hours";
            else return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        else return "In " + minutes + " minutes";
    }
}
