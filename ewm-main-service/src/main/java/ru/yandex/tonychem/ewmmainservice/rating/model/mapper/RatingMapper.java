package ru.yandex.tonychem.ewmmainservice.rating.model.mapper;

import ru.yandex.tonychem.ewmmainservice.rating.model.dto.RatingShortDto;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.EventRatingInfo;
import ru.yandex.tonychem.ewmmainservice.rating.model.entity.Rating;

public class RatingMapper {
    public static RatingShortDto toRatingShortDto(EventRatingInfo info) {
        return new RatingShortDto(info.getEventId(), info.getLikeCount(), info.getDislikesCount());
    }
}
