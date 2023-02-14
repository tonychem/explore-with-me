package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.Value;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.LikeStatus;

@Value
public class RatingDto {
    Long id;
    LikeStatus status;
    Long userId;
    Long eventId;
}
