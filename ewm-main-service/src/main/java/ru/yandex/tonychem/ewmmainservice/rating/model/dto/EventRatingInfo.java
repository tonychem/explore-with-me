package ru.yandex.tonychem.ewmmainservice.rating.model.dto;

public interface EventRatingInfo {
    Long getEventId();
    Integer getLikeCount();
    Integer getDislikeCount();
}
