package ru.yandex.tonychem.statsserver.service;

import dto.EndPointHitDto;
import dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticsService {
    void registerView(EndPointHitDto endPointHitDto);

    Collection<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
