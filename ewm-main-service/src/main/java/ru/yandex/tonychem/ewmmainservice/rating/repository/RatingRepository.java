package ru.yandex.tonychem.ewmmainservice.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Modifying
    @Query("delete from Rating r where r.user.id = :userId and r.event.id = :eventId")
    Integer deleteUserRatingByEventId(long userId, long eventId);
}
