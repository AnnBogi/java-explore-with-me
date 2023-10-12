package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
public class EndpointHitDto {
    private long id;
    private AppName app;
    private String uri;
    private String ip;

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime timestamp;
}