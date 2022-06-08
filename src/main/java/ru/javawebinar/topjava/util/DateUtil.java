package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

    public static String localDateTimeToString(LocalDateTime ldt) {
        return ldt.format(DATE_TIME_FORMATTER);
    }
}
