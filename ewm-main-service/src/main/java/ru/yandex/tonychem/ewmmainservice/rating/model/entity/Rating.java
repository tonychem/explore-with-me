package ru.yandex.tonychem.ewmmainservice.rating.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_ratings",
            joinColumns = {@JoinColumn(name = "rating_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_ratings",
            joinColumns = {@JoinColumn(name = "rating_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Event event;
}
