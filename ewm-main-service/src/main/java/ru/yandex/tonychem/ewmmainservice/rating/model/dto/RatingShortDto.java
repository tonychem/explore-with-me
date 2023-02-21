package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.Value;

@Value
public class RatingShortDto {
    Long eventId;
    Integer likeCount;
    Integer dislikeCount;
}
