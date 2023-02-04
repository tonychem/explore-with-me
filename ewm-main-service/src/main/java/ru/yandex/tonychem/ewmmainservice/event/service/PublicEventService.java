package ru.yandex.tonychem.ewmmainservice.event.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    ResponseEntity<Object> eventsBy(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                    LocalDateTime end, Boolean onlyAvailable, EventSort sort,
                                    Integer from, Integer size);


    ResponseEntity<Object> detailedInfoEvent(long eventId);
}
