package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T leftBorder, T rightBorder) {
        return value.compareTo(leftBorder) >= 0 && value.compareTo(rightBorder) < 0;
    }
//
//    public static boolean isBetweenHalfOpen(LocalDateTime value, LocalDateTime leftBorder, LocalDateTime rightBorder) {
//        return value.compareTo(leftBorder) >= 0 && value.compareTo(rightBorder) < 0;
//    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

