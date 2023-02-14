package ru.yandex.tonychem.ewmmainservice.event.model.entity;

import lombok.*;
import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;
import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String annotation;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime created;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @Column(name = "requested_moderation")
    private Boolean moderationRequested;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(nullable = false)
    private String title;
}
