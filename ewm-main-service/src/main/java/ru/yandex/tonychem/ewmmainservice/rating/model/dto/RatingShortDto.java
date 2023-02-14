package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.Value;

@Value
public class RatingShortDto {
    Long eventId;
    Long likeCount;
    Long dislikeCount;
}
