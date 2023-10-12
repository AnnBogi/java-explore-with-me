package ru.practicum.ewm.stats.server.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UriCountProjection {
    private String uri;
    private Long count;
}