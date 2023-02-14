package ru.yandex.tonychem.ewmmainservice.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.RatingException;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingFullDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;
import ru.yandex.tonychem.ewmmainservice.rating.model.mapper.RatingMapper;
import ru.yandex.tonychem.ewmmainservice.rating.repository.RatingRepository;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;
import ru.yandex.tonychem.ewmmainservice.utils.RatingProcessor;

import java.time.LocalDateTime;
import java.util.List;

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

        if (ratingRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new RatingException("User=" + userId + " already rated event=" + eventId);
        }

        Rating rating = new Rating();
        rating.setStatus(userRatingDto.getStatus());
        rating.setEvent(eventRepository.getReferenceById(eventId));
        rating.setUser(userRepository.getReferenceById(userId));
        rating.setCreationDate(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);
        return new ResponseEntity<>(RatingMapper.toRatingShortDto(savedRating,
                ratingRepository.getRatingCountByEventId(eventId, LikeStatus.LIKE),
                ratingRepository.getRatingCountByEventId(eventId, LikeStatus.DISLIKE)), HttpStatus.CREATED);
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

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<Object> getEventRating(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NoSuchEventException("Event with id=" + eventId + " was not found");
        }

        List<Long> likeList = ratingRepository.getUserListByEventIdAndStatus(eventId, LikeStatus.LIKE);
        List<Long> dislikeList = ratingRepository.getUserListByEventIdAndStatus(eventId, LikeStatus.DISLIKE);

        Double rating = RatingProcessor.calculateScore((long) likeList.size(), (long) dislikeList.size());

        return new ResponseEntity<>(new RatingFullDto(eventId,likeList, dislikeList, rating), HttpStatus.OK);
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
