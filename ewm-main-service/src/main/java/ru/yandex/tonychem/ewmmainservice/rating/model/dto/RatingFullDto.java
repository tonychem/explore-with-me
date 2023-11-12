package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.Value;

import java.util.List;

@Value
public class RatingFullDto {
    Long eventId;
    List<Long> userLikes;
    List<Long> userDislikes;
}
