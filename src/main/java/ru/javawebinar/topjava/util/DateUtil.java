package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static String formatLocalDateTimeToString(LocalDateTime ldt) {
        return ldt.format(DateFormatter.getInstance());
    }

    public static LocalDateTime formatStringToLocalDateTime(String dateTime) {
        String[] dateAndTime = dateTime.split(" ");
        String[] date = dateAndTime[0].split("\\.");
        String[] time = dateAndTime[1].split(":");
        return LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[3]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]));
    }

    static class DateFormatter {
        private static class DateFormatterHolder {
            private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
        }
        public static DateTimeFormatter getInstance() {
            return DateFormatterHolder.DATE_TIME_FORMATTER;
        }
    }
}
