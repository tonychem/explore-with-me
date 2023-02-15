package ru.yandex.tonychem.ewmmainservice.rating.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.EventRatingInfo;

import java.util.List;

@Repository
public interface EventRatingInfoRepository extends JpaRepository<EventRatingInfo, Long> {
    List<EventRatingInfo> findAllByOrderByRatingDesc(Pageable pageable);
}
