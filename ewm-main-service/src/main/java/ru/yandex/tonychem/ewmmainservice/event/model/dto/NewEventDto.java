package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "Field: annotation. Minimum size is 20, maximum size is 2000")
    @NotEmpty(message = "Annotation field is missing")
    private String annotation;

    @NotNull
    private Long category;

    @Size(min = 20, max = 7000, message = "Field: description. Minimum size is 20, maximum size is 7000")
    @NotEmpty(message = "Description field is missing")
    private String description;

    @NotNull(message = "EventDate field is missing")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    @Future(message = "Event must occur in the future")
    private LocalDateTime eventDate;

    @NotNull(message = "Location field is missing")
    private Location location;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean paid = false;

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer participantLimit = 0;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean requestModeration = true;

    @Size(min = 3, max = 120, message = "Field: title. Minimum size is 3, maximum size is 120")
    @NotEmpty(message = "Title field is missing")
    private String title;
}
