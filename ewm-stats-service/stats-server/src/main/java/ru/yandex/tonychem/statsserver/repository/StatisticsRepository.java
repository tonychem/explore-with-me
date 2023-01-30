package ru.yandex.tonychem.statsserver.repository;

import dto.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.tonychem.statsserver.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new dto.ViewStats(eh.app, eh.uri, count(eh)) from EndpointHit eh " +
            "where eh.timestamp between :startTime and :endTime " +
            "and ((:uris) is null or eh.uri in (:uris)) " +
            "group by eh.app, eh.uri")
    List<ViewStats> viewStatsByUrisAndTimestampBetween(LocalDateTime startTime,
                                                       LocalDateTime endTime,
                                                       List<String> uris);

    @Query("select new dto.ViewStats(eh.app, eh.uri, count(eh)) " +
            "from EndpointHit eh where eh.timestamp between :startTime and :endTime " +
            "and ((:uris) is null or eh.uri in (:uris)) " +
            "group by eh.app, eh.uri, eh.ip")
    List<ViewStats> viewStatsOfUniqueIPByUrisAndTimestampBetween(LocalDateTime startTime,
                                                                 LocalDateTime endTime,
                                                                 List<String> uris);
}
