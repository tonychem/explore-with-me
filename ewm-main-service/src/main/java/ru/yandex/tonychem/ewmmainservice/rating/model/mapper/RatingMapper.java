package ru.yandex.tonychem.ewmmainservice.rating.model.mapper;

import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingShortDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

public class RatingMapper {
    public static RatingShortDto toRatingShortDto(Rating rating, Long likeCount, Long dislikeCount) {
        return new RatingShortDto(rating.getEvent().getId(), likeCount, dislikeCount);
    }
}
