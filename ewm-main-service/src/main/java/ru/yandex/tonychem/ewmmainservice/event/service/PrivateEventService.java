package ru.yandex.tonychem.ewmmainservice.event.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventRequestStatusUpdateRequest;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.NewEventDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventUserRequest;

public interface PrivateEventService {
    ResponseEntity<Object> getUserEvents(long userId, Integer from, Integer size);

    ResponseEntity<Object> createEvent(long userId, NewEventDto newEventDto);

    ResponseEntity<Object> getDetailedEventInfo(long userId, long eventId);

    ResponseEntity<Object> updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    ResponseEntity<Object> getParticipationRequests(long userId, long eventId);

    ResponseEntity<Object> updateParticipationRequest(long userId, long eventId,
                                                      EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
