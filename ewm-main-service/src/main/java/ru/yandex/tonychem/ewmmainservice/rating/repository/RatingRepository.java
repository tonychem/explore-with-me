package ru.yandex.tonychem.ewmmainservice.rating.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.EventRatingInfo;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Modifying
    @Query("delete from Rating r where r.user.id = :userId and r.event.id = :eventId")
    Integer deleteUserRatingByEventId(long userId, long eventId);

    Boolean existsByUserIdAndEventId(long userId, long eventId);

    Optional<Rating> findByUserIdAndEventId(long userId, long eventId);

    @Query("select r.user.id from Rating r where r.event.id = :eventId and r.status = :status")
    List<Long> getUserListByEventIdAndStatus(long eventId, LikeStatus status);

    @Query(nativeQuery = true,
            value = "select event_id as eventId, like_count as likeCount, dislike_count as dislikeCount, " +
                    "rating from get_event_rating_info(:eventId)")
    EventRatingInfo getEventRatingInfo(long eventId);

    @Query(nativeQuery = true,
            value = "select get_popular_events()")
    List<Long> getPopularEventIdsOrderDesc(Pageable page);

    @Query(nativeQuery = true,
            value = "select get_popular_compilations()")
    List<Long> getPopularCompilationIdsOrderDesc(Pageable page);
}
