package ru.yandex.tonychem.ewmmainservice.event.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventFullDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventSort;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventState;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.QEvent;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.EventAccessException;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchEventException;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import ru.yandex.tonychem.ewmmainservice.utils.QueryPredicate;
import statistics.client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.MAXIMUM_END_DATE;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;

    private final ParticipationRepository participationRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public ResponseEntity<Object> eventsBy(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime start, LocalDateTime end, Boolean onlyAvailable,
                                           EventSort sort, Integer from, Integer size) {
        if (start == null) {
            start = LocalDateTime.now();
        }

        if (end == null) {
            end = MAXIMUM_END_DATE;
        }

        Predicate predicate = buildQueryDslPredicateBy(text, categories, paid, start, end);
        Pageable pageable = PageRequest.of(from / size, size);

        Stream<EventFullDto> eventList = eventRepository.findAll(predicate, pageable).stream()
                .map(event -> EventMapper.toEventFullDto(event,
                        participationRepository.getConfirmedRequestsByEventId(event.getId()),
                        statisticsClient.getViewCountForEvent(event.getId())))
                .filter(event -> event.getState() == EventState.PUBLISHED);

        if (onlyAvailable) {
            eventList = eventList.filter(event ->
                    event.getParticipantLimit() == 0 || event.getParticipantLimit() - event.getConfirmedRequests() > 0);
        }

        if (sort != null) {
            if (sort == EventSort.VIEWS) {
                eventList = eventList.sorted(Comparator.comparing(EventFullDto::getViews));
            } else if (sort == EventSort.EVENT_DATE) {
                eventList = eventList.sorted(Comparator.comparing(EventFullDto::getEventDate));
            }
        }

        List<EventShortDto> eventShortDtos = eventList.map(EventMapper::fullToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(eventShortDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> detailedInfoEvent(long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        if (event.getState() != EventState.PUBLISHED) {
            throw new EventAccessException("Event must be published");
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event,
                participationRepository.getConfirmedRequestsByEventId(eventId),
                statisticsClient.getViewCountForEvent(eventId));

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    private Predicate buildQueryDslPredicateBy(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        QEvent event = QEvent.event;
        QueryPredicate predicates = QueryPredicate.newQueryPredicate()
                .add(text, (subText) -> {
                    Predicate annotationPredicate = event.annotation.containsIgnoreCase(subText);
                    Predicate descriptionPredicate = event.description.containsIgnoreCase(subText);
                    return ExpressionUtils.anyOf(annotationPredicate, descriptionPredicate);
                })
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .add(paid, event.paid::eq)
                .add(categories, (categoryList) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    categoryList.forEach(id ->
                            predicateList.add(event.category.id.eq(id)));
                    return ExpressionUtils.anyOf(predicateList);
                });
        return predicates.allOf();
    }
}
