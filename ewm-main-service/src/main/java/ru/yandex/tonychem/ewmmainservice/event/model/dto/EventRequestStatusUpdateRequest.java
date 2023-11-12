package ru.yandex.tonychem.ewmmainservice.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.EventStatusAction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotEmpty(message = "Field requestIds must not be empty")
    private List<Long> requestIds;

    @NotNull(message = "Field status must not be null")
    private EventStatusAction status;
}
