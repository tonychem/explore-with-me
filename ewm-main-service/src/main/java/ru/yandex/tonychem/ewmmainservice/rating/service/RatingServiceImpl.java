package ru.yandex.tonychem.ewmmainservice.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.mapper.CompilationMapper;
import ru.yandex.tonychem.ewmmainservice.compilation.repository.CompilationRepository;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.ParticipationRequestInfo;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchRatingException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchUserException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.RatingException;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.EventRatingInfo;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingFullDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.UserRatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;
import ru.yandex.tonychem.ewmmainservice.rating.model.mapper.RatingMapper;
import ru.yandex.tonychem.ewmmainservice.rating.repository.RatingRepository;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;
import statistics.client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RatingRepository ratingRepository;

    private final ParticipationRepository participationRepository;

    private final CompilationRepository compilationRepository;

    private final StatisticsClient statisticsClient;

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
        rating.setCreationDate(LocalDateTime.now());
        rating.setEvent(eventRepository.getReferenceById(eventId));
        rating.setUser(userRepository.getReferenceById(userId));

        ratingRepository.save(rating);

        EventRatingInfo info = ratingRepository.getEventRatingInfo(eventId);

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
        ratingRepository.save(rating);

        EventRatingInfo info = ratingRepository.getEventRatingInfo(eventId);

        return new ResponseEntity<>(RatingMapper.toRatingShortDto(info), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removeRating(long userId, long eventId) {
        ratingRepository.deleteUserRatingByEventId(userId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

        List<Long> popularEventIdsList = ratingRepository.getPopularEventIdsOrderDesc(pageable);

        List<EventShortDto> eventList = eventRepository.findAllById(popularEventIdsList).stream()
                .map(event -> EventMapper.toEventShortDto(event, statisticsClient.getViewCountForEvent(event.getId())))
                .collect(Collectors.toList());

        Map<Long, Integer> eventIdParticipationCount = participationRepository
                .getConfirmedRequestsByEventIdsIn(popularEventIdsList).stream()
                .collect(Collectors.toMap(ParticipationRequestInfo::getEventId,
                        ParticipationRequestInfo::getConfirmedRequests));

        for (EventShortDto eventShortDto : eventList) {
            Integer confirmedRequests = Optional.ofNullable(eventIdParticipationCount.get(eventShortDto.getId()))
                    .orElse(0);
            eventShortDto.setConfirmedRequests(confirmedRequests);
        }

        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<Object> getPopularCompilations(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Long> popularCompilationListIds = ratingRepository.getPopularCompilationIdsOrderDesc(pageable);

        List<CompilationDto> compilationDtoList = compilationRepository.findAllById(popularCompilationListIds)
                .stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation,
                        participationRepository, statisticsClient))
                .collect(Collectors.toList());

        return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
    }
}
