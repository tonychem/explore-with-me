package ru.yandex.tonychem.statsserver.service;

import dto.EndPointHitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.statsserver.entity.EndpointHit;
import ru.yandex.tonychem.statsserver.entity.EndpointHitMapper;
import ru.yandex.tonychem.statsserver.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Override
    public ResponseEntity<Void> registerView(EndPointHitDto endPointHitDto) {
        EndpointHit hit = EndpointHitMapper.toEndPointHit(endPointHitDto);
        statisticsRepository.save(hit);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStatsList;

        if (unique) {
            List<ViewStats> listOfViewByAppAndUriAndIP = statisticsRepository
                    .viewStatsOfUniqueIPByUrisAndTimestampBetween(start, end, uris);

            viewStatsList = listOfViewByAppAndUriAndIP.stream()
                    .distinct()
                    .peek(viewStat ->
                            viewStat.setHits((long) Collections.frequency(listOfViewByAppAndUriAndIP, viewStat)))
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            viewStatsList = statisticsRepository.viewStatsByUrisAndTimestampBetween(start, end, uris).stream()
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(viewStatsList, HttpStatus.OK);
    }

}
