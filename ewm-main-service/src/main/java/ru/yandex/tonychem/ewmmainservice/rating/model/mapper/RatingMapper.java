package ru.yandex.tonychem.ewmmainservice.rating.model.mapper;

import ru.yandex.tonychem.ewmmainservice.rating.model.dto.EventRatingInfo;
import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingShortDto;

public class RatingMapper {
    public static RatingShortDto toRatingShortDto(EventRatingInfo info) {
        return new RatingShortDto(info.getEventId(), info.getLikeCount(), info.getDislikeCount());
    }
}
