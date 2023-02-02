package ru.yandex.tonychem.ewmmainservice.event.service;


import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    ResponseEntity<Object> updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    ResponseEntity<Object> eventsBy(List<Long> users, List<String> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
