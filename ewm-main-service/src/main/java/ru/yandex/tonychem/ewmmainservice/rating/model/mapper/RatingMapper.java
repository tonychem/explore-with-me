package ru.yandex.tonychem.ewmmainservice.rating.model.mapper;

import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

public class RatingMapper {
    public static RatingDto toRatingDto(Rating rating) {
        return new RatingDto(rating.getId(), rating.getStatus(), rating.getUser().getId(),
                rating.getEvent().getId());
    }
}
