package ru.yandex.tonychem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalConstantConfig {
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final LocalDateTime MINIMUM_DATE =
            LocalDateTime.of(1, 1, 1, 0, 0, 0);
    public static final LocalDateTime MAXIMUM_DATE =
            LocalDateTime.of(9999, 12, 31, 23, 59, 59);
}
