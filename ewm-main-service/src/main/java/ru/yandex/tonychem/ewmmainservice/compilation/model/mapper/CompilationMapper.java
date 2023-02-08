package ru.yandex.tonychem.ewmmainservice.compilation.model.mapper;

import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.UpdateCompilationRequest;
import ru.yandex.tonychem.ewmmainservice.compilation.model.entity.Compilation;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.ParticipationRequestInfo;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import statistics.client.StatisticsClient;

import java.util.*;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation,
                                                  ParticipationRepository participationRepository,
                                                  StatisticsClient statisticsClient) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        List<Long> eventIds = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> eventParticipationCount = participationRepository.getConfirmedRequestsByEventIdsIn(eventIds)
                .stream()
                .collect(Collectors.toMap(ParticipationRequestInfo::getEventId,
                        ParticipationRequestInfo::getConfirmedRequests));

        for (Event event : compilation.getEvents()) {
            Integer confirmedRequests = Optional.ofNullable(eventParticipationCount.get(event.getId()))
                            .orElse(0);
            eventShortDtoList.add(EventMapper.toEventShortDto(event, confirmedRequests,
                    statisticsClient.getViewCountForEvent(event.getId())));
        }

        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.getPinned(),
                eventShortDtoList);
    }

    public static Compilation updateCompilationFields(UpdateCompilationRequest updateCompilationRequest,
                                                      Compilation compilation, EventRepository eventRepository) {
        String newTitle = updateCompilationRequest.getTitle();
        Boolean newPinned = updateCompilationRequest.getPinned();
        List<Long> newEvents = updateCompilationRequest.getEvents();

        if (newTitle != null) {
            compilation.setTitle(newTitle);
        }

        if (newPinned != null) {
            compilation.setPinned(newPinned);
        }

        if (newEvents != null) {
            if (newEvents.isEmpty()) {
                compilation.setEvents(Collections.emptyList());
            } else {
                List<Event> eventList = eventRepository.findAllById(newEvents);
                compilation.setEvents(eventList);
            }
        }

        return compilation;
    }
}
