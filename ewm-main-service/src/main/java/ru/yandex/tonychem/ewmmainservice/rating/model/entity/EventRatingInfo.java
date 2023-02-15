package ru.yandex.tonychem.ewmmainservice.rating.model.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "total_rating")
@Getter
public class EventRatingInfo {
    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "likes")
    private Integer likeCount;

    @Column(name = "dislikes")
    private Integer dislikesCount;

    @Column(name = "rating")
    private Double rating;
}
