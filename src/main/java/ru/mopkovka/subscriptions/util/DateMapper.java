package ru.mopkovka.subscriptions.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateMapper {
    private static final DateTimeFormatter EXCEPTION_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getExceptionTimestampString(LocalDateTime localDateTime) {
        return EXCEPTION_FORMATTER.format(localDateTime);
    }
}
