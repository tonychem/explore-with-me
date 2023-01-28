package ru.yandex.tonychem.statsserver.service;

import dto.EndPointHitDto;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.statsserver.entity.EndpointHit;
import ru.yandex.tonychem.statsserver.entity.EndpointHitMapper;
import ru.yandex.tonychem.statsserver.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Override
    public void registerView(EndPointHitDto endPointHitDto) {
        EndpointHit hit = EndpointHitMapper.toEndPointHit(endPointHitDto);
        statisticsRepository.save(hit);
    }

    @Override
    public Collection<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, String uris, Boolean unique) {
        if (unique) {
            //Выгружаем количество посещений по связке app&uri. Один объект - один IP
            List<ViewStats> listOfViewByAppAndUriAndIP = statisticsRepository
                    .countHitsOfUniqueIPByUrisAndTimestampBetween(start, end, uris);

            return listOfViewByAppAndUriAndIP.stream()
                    //уникальность по app&uri
                    .distinct()
                    // каждому объекту viewstat присваиваем количество переходов, равное количеству объектов в списке,
                    // т.е. уникальных ip по связке app&uri
                    .peek(viewStat ->
                            viewStat.setHits((long) Collections.frequency(listOfViewByAppAndUriAndIP, viewStat)))
                    .toList();
        } else {
            return statisticsRepository.countHitsByUrisAndTimestampBetween(start, end, uris);
        }
    }

}
