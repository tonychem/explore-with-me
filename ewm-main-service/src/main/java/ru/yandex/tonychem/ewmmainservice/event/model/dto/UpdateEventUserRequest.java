package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.UserEventAction;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventUserRequest {
    @Min(value = 20, message = "Minimum character limit is 20")
    @Max(value = 2000, message = "Maximum character limit is 2000")
    private String annotation;

    private Long category;

    @Min(value = 20, message = "Minimum character limit is 20")
    @Max(value = 7000, message = "Maximum character limit is 7000")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    @Future(message = "Event must occur in the future")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Min(value = 3, message = "Minimum character limit is 3")
    @Max(value = 120, message = "Maximum character limit is 120")
    private String title;

    private UserEventAction stateAction;
}
