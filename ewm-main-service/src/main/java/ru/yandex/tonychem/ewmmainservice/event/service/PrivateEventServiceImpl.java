package ru.yandex.tonychem.ewmmainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.category.repository.CategoryRepository;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.*;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventState;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventStatusAction;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.*;
import ru.yandex.tonychem.ewmmainservice.participation.model.dto.ParticipationRequestDto;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequest;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequestState;
import ru.yandex.tonychem.ewmmainservice.participation.model.mapper.ParticipationRequestMapper;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;
import statistics.client.StatisticsClient;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticsClient statisticsClient;
    private final ParticipationRepository participationRepository;

    private static final int MINIMUM_HOURS_UNTIL_EVENT_BEGINS = 2;

    @Override
    public ResponseEntity<Object> getUserEvents(long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        Pageable pageable = PageRequest.of(from / size, size);
        List<EventShortDto> eventShortDtosList = eventRepository.findEventsByCreatorId(userId, pageable).stream()
                .map(event -> EventMapper.toEventShortDto(
                        event, statisticsClient.getViewCountForEvent(event.getId()),
                        participationRepository.getConfirmedRequestsByEventId(event.getId())
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventShortDtosList, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> createEvent(long userId, NewEventDto newEventDto) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        if (!categoryRepository.existsById(newEventDto.getCategory())) {
            throw new NoSuchCategoryException("Category with id="
                    + newEventDto.getCategory() + " was not found");
        }

        LocalDateTime now = LocalDateTime.now();

        if (newEventDto.getEventDate().minusHours(MINIMUM_HOURS_UNTIL_EVENT_BEGINS).isBefore(now)) {
            throw new EventCreationException("New event date must be at least " + MINIMUM_HOURS_UNTIL_EVENT_BEGINS +
                    " hours before its creation");
        }

        Event newEvent = new Event();
        newEvent.setAnnotation(newEventDto.getAnnotation());
        newEvent.setCategory(categoryRepository.getReferenceById(newEventDto.getCategory()));
        newEvent.setDescription(newEventDto.getDescription());
        newEvent.setCreated(now);
        newEvent.setEventDate(newEventDto.getEventDate());
        newEvent.setCreator(userRepository.getReferenceById(userId));
        newEvent.setLocation(newEventDto.getLocation());
        newEvent.setPaid(newEventDto.getPaid());
        newEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        newEvent.setModerationRequested(newEventDto.getRequestModeration());
        newEvent.setTitle(newEventDto.getTitle());
        newEvent.setState(EventState.PENDING);

        Event savedEvent = eventRepository.save(newEvent);
        EventFullDto savedEventFullDto = EventMapper.toEventFullDto(savedEvent,
                participationRepository.getConfirmedRequestsByEventId(savedEvent.getId()),
                statisticsClient.getViewCountForEvent(savedEvent.getId()));

        return new ResponseEntity<>(savedEventFullDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getDetailedEventInfo(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        if (event.getCreator().getId() != userId) {
            throw new EventAccessException("User=" + userId + " does not own the event=" + eventId);
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event,
                participationRepository.getConfirmedRequestsByEventId(eventId),
                statisticsClient.getViewCountForEvent(eventId));

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateEvent(long userId, long eventId,
                                              UpdateEventUserRequest updateEventUserRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        if (event.getCreator().getId() != userId) {
            throw new EventAccessException("User=" + userId + " does not own the event=" + eventId);
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new EventUpdateException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getCategory() != null) {
            if (categoryRepository.existsById(updateEventUserRequest.getCategory())) {
                event.setCategory(categoryRepository.getReferenceById(updateEventUserRequest.getCategory()));
            } else {
                throw new NoSuchCategoryException("Category with id="
                        + updateEventUserRequest.getCategory() + " was not found");
            }
        }

        if (updateEventUserRequest.getEventDate() != null) {
            if (!updateEventUserRequest.getEventDate().minusHours(MINIMUM_HOURS_UNTIL_EVENT_BEGINS)
                    .isBefore(LocalDateTime.now())) {
                event.setEventDate(updateEventUserRequest.getEventDate());
            } else {
                throw new EventUpdateException("New event date must be at least " + MINIMUM_HOURS_UNTIL_EVENT_BEGINS +
                        " h before modification");
            }
        }

        String newAnnotation = updateEventUserRequest.getAnnotation();
        String newDescription = updateEventUserRequest.getDescription();
        Location newLocation = updateEventUserRequest.getLocation();
        Boolean newPaid = updateEventUserRequest.getPaid();
        Integer newParticipantLimit = updateEventUserRequest.getParticipantLimit();
        Boolean newRequestModeration = updateEventUserRequest.getRequestModeration();
        String newTitle = updateEventUserRequest.getTitle();

        if (newAnnotation != null) {
            event.setAnnotation(newAnnotation);
        }

        if (newDescription != null) {
            event.setDescription(newDescription);
        }

        if (newLocation != null) {
            event.setLocation(newLocation);
        }

        if (newPaid != null) {
            event.setPaid(newPaid);
        }

        if (newParticipantLimit != null) {
            event.setParticipantLimit(newParticipantLimit);
        }

        if (newRequestModeration != null) {
            event.setModerationRequested(newRequestModeration);
        }

        if (newTitle != null) {
            event.setTitle(newTitle);
        }

        switch (updateEventUserRequest.getStateAction()) {
            case CANCEL_REVIEW:
                event.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
        }

        Event savedEvent = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(savedEvent,
                participationRepository.getConfirmedRequestsByEventId(savedEvent.getId()),
                statisticsClient.getViewCountForEvent(savedEvent.getId()));
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getParticipationRequests(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NoSuchEventException("Event with id=" + eventId + " was not found");
        }

        List<ParticipationRequestDto> requests = participationRepository.getRequestsToEventOfUser(userId, eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateParticipationRequest(
            long userId, long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        if (event.getCreator().getId() != userId) {
            throw new EventAccessException("User=" + userId + " does not own the event=" + eventId);
        }

        List<ParticipationRequest> requests = participationRepository
                .findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != ParticipationRequestState.PENDING) {
                throw new ParticipationRequestUpdateException("Participation request=" + request.getId() +
                        " is already " + "either approved or canceled");
            }
        }

        if (event.getParticipantLimit() == 0 || !event.getModerationRequested()) {
            for (ParticipationRequest request : requests) {
                request.setStatus(ParticipationRequestState.CONFIRMED);
            }
            participationRepository.saveAll(requests);
            List<ParticipationRequestDto> accepted = requests.stream()
                    .map(ParticipationRequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new EventRequestStatusUpdateResult(accepted, Collections.emptyList()),
                    HttpStatus.OK);
        }

        List<ParticipationRequestDto> acceptedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getStatus() == EventStatusAction.CONFIRMED) {

            int emptySeats = event.getParticipantLimit() -
                    participationRepository.getConfirmedRequestsByEventId(eventId);

            if (emptySeats > 0) {

                int acceptedRequestsCounter = emptySeats;
                int requestsListIndex = 0;

                while (acceptedRequestsCounter > 0 && requestsListIndex < requests.size()) {
                    ParticipationRequest currentRequest = requests.get(requestsListIndex);
                    currentRequest.setStatus(ParticipationRequestState.CONFIRMED);
                    acceptedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(currentRequest));
                    acceptedRequestsCounter--;
                    requestsListIndex++;
                }

                for (int i = requestsListIndex; i < requests.size(); i++) {
                    ParticipationRequest currentRequest = requests.get(requestsListIndex);
                    currentRequest.setStatus(ParticipationRequestState.REJECTED);
                    rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(currentRequest));
                }
            } else {
                throw new EventUpdateException("The participant limit has been reached");
            }
        } else {
            for (ParticipationRequest request : requests) {
                request.setStatus(ParticipationRequestState.REJECTED);
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            }
        }

        participationRepository.saveAll(requests);

        return new ResponseEntity<>(new EventRequestStatusUpdateResult(acceptedRequests, rejectedRequests),
                HttpStatus.OK);
    }
}
