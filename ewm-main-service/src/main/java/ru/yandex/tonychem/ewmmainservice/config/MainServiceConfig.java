package ru.yandex.tonychem.ewmmainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import statistics.client.StatisticsClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class MainServiceConfig {
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Value("${statistics.server-url}")
    private String statisticsUrl;
    public static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public static final LocalDateTime MAXIMUM_END_DATE = LocalDateTime.of(2999, 12, 12,
            23, 59, 59);

    @Bean
    public StatisticsClient statisticsClient() {
        return new StatisticsClient(statisticsUrl);
    }
}
