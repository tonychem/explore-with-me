package ru.yandex.tonychem.ewmmainservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import statistics.client.StatisticsClient;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class MainServiceConfig {
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    @Bean
    public StatisticsClient statisticsClient() {
        return new StatisticsClient();
    }
}
