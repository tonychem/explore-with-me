package ru.yandex.tonychem.ewmmainservice.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;
import ru.yandex.tonychem.ewmmainservice.rating.model.mapper.RatingMapper;
import ru.yandex.tonychem.ewmmainservice.rating.repository.RatingRepository;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> rateEvent(long userId, long eventId, UserRatingDto userRatingDto) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NoSuchEventException("Event with id=" + eventId + " was not found");
        }

        Rating rating = new Rating();
        rating.setStatus(userRatingDto.getStatus());
        rating.setEvent(eventRepository.getReferenceById(eventId));
        rating.setUser(userRepository.getReferenceById(userId));

        Rating savedRating = ratingRepository.save(rating);
        return new ResponseEntity<>(RatingMapper.toRatingDto(savedRating), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> removeRating(long userId, long eventId) {
        ratingRepository.deleteUserRatingByEventId(userId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Object> getRecommendations(long userId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getEventRating(long eventId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getPopularEvents(Integer from, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getCompilationRating(long compId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getPopularCompilations(Integer from, Integer size) {
        return null;
    }
}
