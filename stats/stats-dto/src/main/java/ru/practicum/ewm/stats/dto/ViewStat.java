package ru.practicum.ewm.stats.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ViewStat {
    private AppName app;

    private String uri;

    private long hits;
}