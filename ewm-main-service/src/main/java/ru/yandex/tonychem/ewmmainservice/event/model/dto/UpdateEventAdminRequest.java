package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventAction;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Location;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventAdminRequest {
    @Min(value = 20, message = "Field: annotation. Minimum character limit is 20")
    @Max(value = 2000, message = "Field: annotation. Maximum character limit is 2000")
    private String annotation;

    private Long category;

    @Min(value = 20, message = "Field: description. Minimum character limit is 20")
    @Max(value = 7000, message = "Field: description. Maximum character limit is 7000")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Min(value = 3, message = "Field: title. Minimum character limit is 3")
    @Max(value = 120, message = "Field: title. Maximum character limit is 120")
    private String title;

    private EventAction stateAction;
}
