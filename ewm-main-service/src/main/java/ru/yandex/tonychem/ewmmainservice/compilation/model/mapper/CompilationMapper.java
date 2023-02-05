package ru.yandex.tonychem.ewmmainservice.compilation.model.mapper;

import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.entity.Compilation;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.mapper.EventMapper;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import statistics.client.StatisticsClient;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation,
                                                  ParticipationRepository participationRepository,
                                                  StatisticsClient statisticsClient) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        for (Event event : compilation.getEvents()) {
            eventShortDtoList.add(EventMapper.toEventShortDto(event,
                    participationRepository.getConfirmedRequestsByEventId(event.getId()),
                    statisticsClient.getViewCountForEvent(event.getId())));
        }

        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.getPinned(),
                eventShortDtoList);
    }
}
