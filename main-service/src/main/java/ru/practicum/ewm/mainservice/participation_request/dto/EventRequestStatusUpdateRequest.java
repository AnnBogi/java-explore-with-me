package ru.practicum.ewm.mainservice.participation_request.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    @NotNull
    @Size(min = 1)
    List<Long> requestIds;

    @NotNull
    Status status;

    public enum Status {
        CONFIRMED, REJECTED
    }

}