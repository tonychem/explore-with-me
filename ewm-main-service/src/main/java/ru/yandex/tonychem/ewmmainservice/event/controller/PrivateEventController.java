package ru.yandex.tonychem.ewmmainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventRequestStatusUpdateRequest;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.NewEventDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventUserRequest;
import ru.yandex.tonychem.ewmmainservice.event.service.PrivateEventService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getUserEvents(@PathVariable long userId,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return privateEventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> createEvent(@PathVariable long userId,
                                              @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getDetailedEventInfo(@PathVariable long userId,
                                                       @PathVariable long eventId) {
        return privateEventService.getDetailedEventInfo(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable long userId,
                                              @PathVariable long eventId,
                                              @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return privateEventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getParticipationRequests(@PathVariable long userId,
                                                           @PathVariable long eventId) {
        return privateEventService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> updateParticipationRequest(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        return privateEventService.updateParticipationRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
