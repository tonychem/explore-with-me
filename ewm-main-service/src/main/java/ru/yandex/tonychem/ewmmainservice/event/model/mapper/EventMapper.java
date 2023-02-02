package ru.yandex.tonychem.ewmmainservice.event.model.mapper;


import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryMapper;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventFullDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserMapper;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return new EventFullDto(event.getId(), event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                confirmedRequests, event.getCreated(), event.getDescription(), event.getEventDate(),
                UserMapper.toUserShortDto(event.getUser()), event.getLocation(),
                event.getPaid(), event.getParticipantLimit(), event.getPublicationDate(),
                event.isModerationRequested(), event.getState(), event.getTitle(), views);
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()), confirmedRequests,
                event.getEventDate(), event.getId(), UserMapper.toUserShortDto(event.getUser()),
                event.getPaid(), event.getTitle(), views);
    }

    public static EventShortDto fullToShortDto(EventFullDto eventFullDto) {
        return new EventShortDto(eventFullDto.getAnnotation(), eventFullDto.getCategory(),
                eventFullDto.getConfirmedRequests(), eventFullDto.getEventDate(), eventFullDto.getId(),
                eventFullDto.getInitiator(), eventFullDto.getPaid(), eventFullDto.getTitle(), eventFullDto.getViews());
    }
}
