package ru.yandex.tonychem.ewmmainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query("select new java.lang.Long(e.user.id) from Event e " +
            "where e.user.id <> :creatorId")
    List<Long> findEventIdByCreatorIdNot(long creatorId);
}
