package ru.yandex.tonychem.ewmmainservice.config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MainServiceConfig {
    public static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
}
