package ru.yandex.tonychem.ewmmainservice.event.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;
import ru.yandex.tonychem.ewmmainservice.category.repository.CategoryRepository;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventFullDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.ParticipationRequestInfo;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventAdminRequest;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventAction;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventState;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.QEvent;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.EventUpdateException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchCategoryException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import ru.yandex.tonychem.ewmmainservice.utils.QueryPredicate;
import statistics.client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private static final int MINIMUM_HOURS_BETWEEN_PUBLICATION_AND_EVENT = 1;
    private final EventRepository eventRepository;
    private final StatisticsClient statisticsClient;
    private final CategoryRepository categoryRepository;

    private final ParticipationRepository participationRepository;

    @Transactional
    @Override
    public ResponseEntity<Object> updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventToBeUpdated = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        Long newCategory = updateEventAdminRequest.getCategory();
        LocalDateTime newEventDate = updateEventAdminRequest.getEventDate();
        EventAction action = updateEventAdminRequest.getStateAction();

        if (action != null) {
            if (eventToBeUpdated.getState() != EventState.PENDING) {
                throw new EventUpdateException("Cannot publish the event because it's not in the right state: " +
                        eventToBeUpdated.getState().toString().toUpperCase());
            }

            if (action == EventAction.PUBLISH_EVENT) {
                if (newEventDate != null) {
                    if (newEventDate.minusHours(MINIMUM_HOURS_BETWEEN_PUBLICATION_AND_EVENT)
                            .isAfter(LocalDateTime.now())) {
                        eventToBeUpdated.setEventDate(newEventDate);
                        eventToBeUpdated.setPublicationDate(LocalDateTime.now());
                        eventToBeUpdated.setState(EventState.PUBLISHED);
                    } else {
                        throw new EventUpdateException("Cannot publish the event because event date is less than " +
                                MINIMUM_HOURS_BETWEEN_PUBLICATION_AND_EVENT + "h before its publication");
                    }
                } else {
                    if (eventToBeUpdated.getEventDate().minusHours(MINIMUM_HOURS_BETWEEN_PUBLICATION_AND_EVENT)
                            .isAfter(LocalDateTime.now())) {
                        eventToBeUpdated.setPublicationDate(LocalDateTime.now());
                        eventToBeUpdated.setState(EventState.PUBLISHED);
                    } else {
                        throw new EventUpdateException("Cannot publish the event because event date is less than " +
                                MINIMUM_HOURS_BETWEEN_PUBLICATION_AND_EVENT + "h before its publication");
                    }
                }
            } else {
                eventToBeUpdated.setState(EventState.CANCELED);
            }
        }

        if (newEventDate != null) {
            if (eventToBeUpdated.getCreated().isAfter(newEventDate)) {
                throw new EventUpdateException("Cannot publish the event because event date is followed by " +
                        "its creation date");
            } else {
                eventToBeUpdated.setEventDate(newEventDate);
            }
        }

        if (newCategory != null) {
            Category category = categoryRepository.findById(newCategory).orElseThrow(
                    () -> new NoSuchCategoryException("Category with id="
                            + newCategory + " was not found"));
            eventToBeUpdated.setCategory(category);
        }

        eventToBeUpdated = EventMapper.updateEventFields(updateEventAdminRequest, eventToBeUpdated);

        Event savedEvent = eventRepository.save(eventToBeUpdated);
        return new ResponseEntity<>(
                EventMapper.toEventFullDto(savedEvent,
                        participationRepository.getConfirmedRequestsByEventId(eventId),
                        statisticsClient.getViewCountForEvent(eventId)), HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> eventsBy(List<Long> users, List<String> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                           Integer size) {
        Predicate predicate = buildQueryDslPredicateBy(users, states, categories, rangeStart, rangeEnd);
        Pageable pageable = PageRequest.of(from / size, size);
        Stream<Event> eventStream;

        if (predicate == null) {
            eventStream = eventRepository.findAll(pageable).stream();
        } else {
            eventStream = eventRepository.findAll(predicate, pageable).stream();
        }

        List<EventFullDto> foundEvents = eventStream
                .map(event -> EventMapper.toEventFullDto(event, statisticsClient.getViewCountForEvent(event.getId())))
                .collect(Collectors.toList());

        List<Long> foundEventIds = foundEvents.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> eventIdParticipationCount = participationRepository
                .getConfirmedRequestsByEventIdsIn(foundEventIds).stream()
                .collect(Collectors.toMap(ParticipationRequestInfo::getEventId,
                        ParticipationRequestInfo::getConfirmedRequests));

        for (EventFullDto event : foundEvents) {
            Integer confirmedRequests = Optional.ofNullable(eventIdParticipationCount.get(event.getId()))
                            .orElse(0);
            event.setConfirmedRequests(confirmedRequests);
        }

        return new ResponseEntity<>(foundEvents, HttpStatus.OK);
    }

    private Predicate buildQueryDslPredicateBy(List<Long> users, List<String> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        QEvent event = QEvent.event;
        QueryPredicate predicates = QueryPredicate.newQueryPredicate()
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .add(users, (userIds) -> {
                    List<Predicate> userIdPredicates = userIds.stream()
                            .map(event.creator.id::eq)
                            .collect(Collectors.toList());
                    return ExpressionUtils.anyOf(userIdPredicates);
                })
                .add(categories, (categoryIds) -> {
                    List<Predicate> categoryIdPredicates = categoryIds.stream()
                            .map(event.category.id::eq)
                            .collect(Collectors.toList());
                    return ExpressionUtils.anyOf(categoryIdPredicates);
                })
                .add(states, (stateList) -> {
                    List<Predicate> statePredicates = stateList.stream()
                            .map(EventState::valueOf)
                            .map(event.state::eq)
                            .map(x -> (Predicate) x)
                            .collect(Collectors.toList());
                    return ExpressionUtils.anyOf(statePredicates);
                });

        return predicates.allOf();
    }
}
