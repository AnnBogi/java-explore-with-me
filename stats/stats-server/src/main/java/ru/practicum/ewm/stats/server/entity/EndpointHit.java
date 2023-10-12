package ru.practicum.ewm.stats.server.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.ewm.stats.dto.AppName;

import javax.persistence.*;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
@Entity
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    private AppName app;

    private String ip;

    private String uri;

    private LocalDateTime timestamp;
}