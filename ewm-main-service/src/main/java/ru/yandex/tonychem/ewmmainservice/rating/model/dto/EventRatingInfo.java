package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRatingInfo {
    private Long eventId;
    private Integer likeCount;
    private Integer dislikeCount;
    private Double rating;
}
