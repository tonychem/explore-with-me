package ru.yandex.tonychem.ewmmainservice.participation.service;

import org.springframework.http.ResponseEntity;

public interface ParticipationService {

    ResponseEntity<Object> userParticipationRequests(long userId);

    ResponseEntity<Object> publishRequest(long userId, long eventId);

    ResponseEntity<Object> cancel(long userId, long requestId);
}
