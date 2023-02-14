package ru.yandex.tonychem.ewmmainservice.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.ParticipationRequestInfo;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query("select count(pr) from ParticipationRequest pr " +
            "where pr.event.id = :eventId and pr.status = 'CONFIRMED'")
    Integer getConfirmedRequestsByEventId(Long eventId);

    @Query("select new ru.yandex.tonychem.ewmmainservice.event.model.dto.ParticipationRequestInfo(pr.event.id, cast(count(pr) as int))" +
            " from ParticipationRequest pr where pr.event.id in (:eventIds) and pr.status = 'CONFIRMED' " +
            "group by pr.event.id")
    List<ParticipationRequestInfo> getConfirmedRequestsByEventIdsIn(List<Long> eventIds);

    @Query("select pr from ParticipationRequest pr " +
            "where pr.participant.id = :participantId and pr.event.creator.id <> :participantId")
    List<ParticipationRequest> getRequestsOnOthersEvents(long participantId);

    Boolean existsByParticipantIdAndEventId(long participantId, long eventId);

    @Query("select pr from ParticipationRequest pr " +
            "where pr.event.id = :eventId and pr.event.creator.id = :userId")
    List<ParticipationRequest> getRequestsToEventOfUser(long userId, long eventId);
}
