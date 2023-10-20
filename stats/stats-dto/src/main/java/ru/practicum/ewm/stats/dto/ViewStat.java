package ru.practicum.ewm.stats.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ViewStat {
    AppName app;

    String uri;

    long hits;
}