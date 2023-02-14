package ru.yandex.tonychem.ewmmainservice.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Modifying
    @Query("delete from Rating r where r.user.id = :userId and r.event.id = :eventId")
    Integer deleteUserRatingByEventId(long userId, long eventId);

    Boolean existsByUserIdAndEventId(long userId, long eventId);

    @Query("select count(r) from Rating r where r.event.id = :eventId and r.status =: status")
    Long getRatingCountByEventId(long eventId, LikeStatus status);

    @Query("select r.user.id from Rating r where r.event.id = :eventId and r.status =: status")
    List<Long> getUserListByEventIdAndStatus(long eventId, LikeStatus status);
}
