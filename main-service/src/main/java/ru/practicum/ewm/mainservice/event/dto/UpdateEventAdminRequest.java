package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.util.Marker;
import ru.practicum.ewm.mainservice.validation.FutureDateTime;
import ru.practicum.ewm.stats.dto.Variables;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventAdminRequest extends NewEventDto {

    private AdminUpdateEventState stateAction;

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    @FutureDateTime(hours = 1, message = "Date must be at least 1 hours in the future", groups = Marker.OnUpdate.class)
    private LocalDateTime eventDate;

}