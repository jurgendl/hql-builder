package org.tools.hqlbuilder.common;

import java.time.Duration;

public class Time8 {
    public static String print(Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.toMillis() / 1000;
        duration = duration.minusSeconds(seconds);
        long millis = duration.toMillis();
        StringBuilder formatted = new StringBuilder();
        if ((days != 0) || (formatted.length() > 0)) {
            formatted.append(days).append("d");
        }
        if ((hours != 0) || (formatted.length() > 0)) {
            formatted.append(hours).append("h");
        }
        if ((minutes != 0) || (formatted.length() > 0)) {
            formatted.append(minutes).append("m");
        }
        if ((seconds != 0) || (formatted.length() > 0)) {
            if (millis > 0) {
                formatted.append(seconds).append(".").append(millis).append("s");
            } else {
                formatted.append(seconds).append("s");
            }
        }
        return formatted.toString();
    }
}
