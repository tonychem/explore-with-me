package ru.yandex.tonychem.ewmmainservice.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventState;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.*;
import ru.yandex.tonychem.ewmmainservice.participation.model.dto.ParticipationRequestDto;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequest;
import ru.yandex.tonychem.ewmmainservice.participation.model.entity.ParticipationRequestState;
import ru.yandex.tonychem.ewmmainservice.participation.model.mapper.ParticipationRequestMapper;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Object> userParticipationRequests(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("No user with id=" + userId);
        }

        List<ParticipationRequestDto> participationRequestDtoList =
                participationRepository.getRequestsOnOthersEvents(userId).stream()
                        .map(ParticipationRequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
        return new ResponseEntity<>(participationRequestDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> publishRequest(long userId, long eventId) {
        if (participationRepository.existsByParticipantIdAndEventId(userId, eventId)) {
            throw new RequestAlreadyExistsException("Request for event=" + eventId + " by user=" + userId +
                    " already exists");
        }

        User requester = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchUserException("No user with id=" + userId)
        );

        Event requestedEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new NoSuchEventException("Event with id=" + eventId + " was not found")
        );

        if (requestedEvent.getCreator().getId() == userId) {
            throw new IllegalParticipationRequestStateException("Self-requests are restricted.");
        }

        if (requestedEvent.getState() != EventState.PUBLISHED) {
            throw new IllegalParticipationRequestStateException("Requests are allowed only for published events.");
        }

        if (requestedEvent.getParticipantLimit() != 0
                && requestedEvent.getParticipantLimit() ==
                participationRepository.getConfirmedRequestsByEventId(eventId)) {
            throw new IllegalParticipationRequestStateException("Event is full.");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();

        participationRequest.setEvent(requestedEvent);
        participationRequest.setParticipant(requester);
        participationRequest.setCreated(LocalDateTime.now());

        if (requestedEvent.getModerationRequested()) {
            participationRequest.setStatus(ParticipationRequestState.PENDING);
        } else {
            participationRequest.setStatus(ParticipationRequestState.CONFIRMED);
        }

        ParticipationRequest savedParticipationRequest = participationRepository.save(participationRequest);

        return new ResponseEntity<>(ParticipationRequestMapper.toParticipationRequestDto(savedParticipationRequest),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> cancel(long userId, long requestId) {
        ParticipationRequest participationRequest = participationRepository.findById(requestId).orElseThrow(
                () -> new NoSuchParticipationRequestException("Request with id=" + requestId + " was not found")
        );

        if (participationRequest.getParticipant().getId() != userId) {
            throw new ParticipationRequestUpdateException("User=" + userId + " is not the request owner");
        }

        participationRequest.setStatus(ParticipationRequestState.CANCELED);
        ParticipationRequest savedParticipationRequest = participationRepository.save(participationRequest);

        return new ResponseEntity<>(ParticipationRequestMapper.toParticipationRequestDto(savedParticipationRequest),
                HttpStatus.OK);
    }
}
