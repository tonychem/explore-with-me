package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryDto;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.yandex.tonychem.ewmmainservice.config.MainServiceConfig.DATETIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
