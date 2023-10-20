package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.event.State;
import ru.practicum.ewm.stats.dto.Variables;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto extends ResponseEventDto {

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    private long id;

    private int participantLimit;

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;
}