package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static LocalDateTime EMPTY = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String localDateTimeToHtml(LocalDateTime localDateTime) {
        if (EMPTY.equals(localDateTime)) {
            return LocalDateTime.now().format(DATE_TIME_FORMATTER);
        }
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
