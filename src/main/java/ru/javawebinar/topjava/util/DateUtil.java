package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static String formatLocalDateTime(LocalDateTime ldt) {
        return ldt.format(DateFormatter.getInstance());
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
