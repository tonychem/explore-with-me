package ru.yandex.tonychem.ewmmainservice.rating.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;

public interface RatingService {
    ResponseEntity<Object> getPersonalEventRating(long userId, long eventId);
    ResponseEntity<Object> rateEvent(long userId, long eventId, UserRatingDto userRatingDto);

    ResponseEntity<Void> removeRating(long userId, long eventId);

    ResponseEntity<Object> updateRating(long userId, long eventId, UserRatingDto userRatingDto);

    ResponseEntity<Object> getEventRating(long eventId);

    ResponseEntity<Object> getPopularEvents(Integer from, Integer size);

    ResponseEntity<Object> getPopularCompilations(Integer from, Integer size);
}
