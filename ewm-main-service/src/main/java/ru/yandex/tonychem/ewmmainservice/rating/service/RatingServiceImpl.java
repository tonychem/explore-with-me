package ru.yandex.tonychem.ewmmainservice.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchRatingException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.RatingException;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingFullDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingShortDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.EventRatingInfo;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;
import ru.yandex.tonychem.ewmmainservice.rating.model.mapper.RatingMapper;
import ru.yandex.tonychem.ewmmainservice.rating.repository.EventRatingInfoRepository;
import ru.yandex.tonychem.ewmmainservice.rating.repository.RatingRepository;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final EventRatingInfoRepository eventRatingInfoRepository;

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

        EventRatingInfo info = eventRatingInfoRepository.findById(eventId).get();

        return new ResponseEntity<>(RatingMapper.toRatingShortDto(info), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateRating(long userId, long eventId, UserRatingDto userRatingDto) {
        Rating rating = ratingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(
                () -> new NoSuchRatingException("User=" + userId + " does not have an existing rating for event="
                        + eventId)
        );

        rating.setStatus(userRatingDto.getStatus());
        Rating savedRating = ratingRepository.save(rating);
        EventRatingInfo info = eventRatingInfoRepository.findById(eventId).get();

        return new ResponseEntity<>(RatingMapper.toRatingShortDto(info), HttpStatus.OK);
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

        return new ResponseEntity<>(new RatingFullDto(eventId, likeList, dislikeList), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<Object> getPopularEvents(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<RatingShortDto> eventsByRatingList = eventRatingInfoRepository.findAllByOrderByRatingDesc(pageable)
                .stream()
                .map(RatingMapper::toRatingShortDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(eventsByRatingList, HttpStatus.OK);
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
