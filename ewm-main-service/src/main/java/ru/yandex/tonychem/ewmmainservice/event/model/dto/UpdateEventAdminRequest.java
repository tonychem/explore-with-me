package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventAction;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Field: annotation. Minimum size is 20, maximum size is 2000")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Field: description. Minimum size is 20, maximum size is 7000")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Field: title. Minimum size is 3, maximum size is 120")
    private String title;

    private EventAction stateAction;
}
