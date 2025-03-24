package me.syncwrld.minecraft.launchmania.tool.time;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {
    public static String formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds < 0) seconds = 0;

        long milliseconds = TimeUnit.MILLISECONDS.toMillis(millis);
        if (milliseconds < 0) milliseconds = 0;

        StringBuilder formattedTime = new StringBuilder();
        if (days > 0) formattedTime.append(days).append("d ");
        if (hours > 0) formattedTime.append(hours).append("h ");
        if (minutes > 0) formattedTime.append(minutes).append("m ");
//        if (seconds > 0 || formattedTime.length() == 0) formattedTime.append(seconds).append("s");

        if (seconds > 0 || formattedTime.length() == 0) {
            if (milliseconds > 0) {
                formattedTime.append(seconds).append(".").append(milliseconds).append("s");
                return formattedTime.toString();
            } else {
                formattedTime.append(seconds).append("s");
            }
        }


        return formattedTime.toString();
    }

    public static String formatDuration(Duration duration) {
        return formatDuration(duration.toMillis());
    }
}