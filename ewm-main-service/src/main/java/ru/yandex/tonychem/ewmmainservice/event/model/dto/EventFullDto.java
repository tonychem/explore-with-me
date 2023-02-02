package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryDto;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventState;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventFullDto {
    private long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
