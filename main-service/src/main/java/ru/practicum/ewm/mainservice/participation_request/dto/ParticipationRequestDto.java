package ru.practicum.ewm.mainservice.participation_request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.participation_request.Status;
import ru.practicum.ewm.stats.dto.Variables;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {

    private long id;

    private long event;

    private long requester;

    private Status status;

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime created;

}