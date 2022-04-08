package com.example.superdupercoolplantapp.background;

import com.example.superdupercoolplantapp.background.models.Reading;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utilities {

    public static LocalDateTime stringToLocalDateTime(String raw) {
        if (raw == null) return null;
        else return LocalDateTime.parse(raw,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getHowLongAgo(LocalDateTime dateTime) {
        if (dateTime != null) {
            Duration duration = Duration.between(LocalDateTime.now(), dateTime);
            int minutes = (int) Math.abs(duration.getSeconds() / 60);
            if (minutes < 1) return "A few moments ago";
            else if (minutes == 1) return "A minute ago";
            else if (minutes >= 60) {
                int hours = minutes / 60;
                if (hours == 1) return "An hour ago";
                else if (hours < 24) return hours + " hours ago";
                else
                    return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else return minutes + " minutes ago";
        } else return "N/A";
    }

    public static String getInHowLong(LocalDateTime dateTime) {
        if (dateTime != null) {
            Duration duration = Duration.between(LocalDateTime.now(), dateTime);
            int minutes = (int) Math.abs(duration.getSeconds() / 60);
            if (minutes < 1) return "In a few moments";
            else if (minutes == 1) return "In a minute";
            else if (minutes >= 60) {
                int hours = minutes / 60;
                if (hours == 1) return "In one hour";
                else if (hours < 24) return "In " + hours + " hours";
                else
                    return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else return "In " + minutes + " minutes";
        } else return "N/A";
    }

    public static String getFormattedTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            LocalDate today = LocalDate.now();
            if (today.isEqual(dateTime.toLocalDate()))
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));               // same date
            else
                return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));    // different date
        } else return "N/A";
    }

    public static String getFormattedReadings(Reading reading) {
        if (reading != null) {
            String tankStatus = (reading.isTankEmpty())? "Empty" : "Full";

            return String.format(Locale.UK,"Temperature: %.0f °C\nWater tank: %s\nLight: %.0f lux",
                    reading.getTempValue(), tankStatus, reading.getLightValue());
        } else return "N/A";
    }
}
