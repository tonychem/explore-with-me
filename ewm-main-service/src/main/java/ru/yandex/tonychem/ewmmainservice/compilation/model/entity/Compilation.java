package ru.yandex.tonychem.ewmmainservice.compilation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Boolean pinned;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "events_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;
}
