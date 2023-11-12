package ru.yandex.tonychem.statsserver.service;

import dto.EndPointHitDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    ResponseEntity<Void> registerView(EndPointHitDto endPointHitDto);

    ResponseEntity<Object> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
