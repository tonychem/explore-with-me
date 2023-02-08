package ru.yandex.tonychem.ewmmainservice.event.model.mapper;


import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryMapper;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventFullDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.EventShortDto;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventAdminRequest;
import ru.yandex.tonychem.ewmmainservice.event.model.dto.UpdateEventUserRequest;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserMapper;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event, Integer confirmedRequests, Integer views) {
        return new EventFullDto(event.getId(), event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                confirmedRequests, event.getCreated(), event.getDescription(), event.getEventDate(),
                UserMapper.toUserShortDto(event.getCreator()), event.getLocation(),
                event.getPaid(), event.getParticipantLimit(), event.getPublicationDate(),
                event.getModerationRequested(), event.getState(), event.getTitle(), views);
    }

    public static EventFullDto toEventFullDto(Event event, Integer views) {
        return toEventFullDto(event, 0, views);
    }

    public static EventShortDto toEventShortDto(Event event, Integer confirmedRequests, Integer views) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()), confirmedRequests,
                event.getEventDate(), event.getId(), UserMapper.toUserShortDto(event.getCreator()),
                event.getPaid(), event.getTitle(), views);
    }

    public static EventShortDto toEventShortDto(Event event, Integer views) {
        return toEventShortDto(event, 0, views);
    }

    public static EventShortDto fullToShortDto(EventFullDto eventFullDto) {
        return new EventShortDto(eventFullDto.getAnnotation(), eventFullDto.getCategory(),
                eventFullDto.getConfirmedRequests(), eventFullDto.getEventDate(), eventFullDto.getId(),
                eventFullDto.getInitiator(), eventFullDto.getPaid(), eventFullDto.getTitle(), eventFullDto.getViews());
    }

    public static Event updateEventFields(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        String newAnnotation = updateEventAdminRequest.getAnnotation();
        String newDescription = updateEventAdminRequest.getDescription();
        Location newLocation = updateEventAdminRequest.getLocation();
        Boolean newPaid = updateEventAdminRequest.getPaid();
        Integer newParticipantLimit = updateEventAdminRequest.getParticipantLimit();
        Boolean newRequestModeration = updateEventAdminRequest.getRequestModeration();
        String newTitle = updateEventAdminRequest.getTitle();

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

        return event;
    }

    public static Event updateEventFields(UpdateEventUserRequest updateEventUserRequest, Event event) {
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

        return event;
    }
}
