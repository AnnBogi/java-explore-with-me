package ru.practicum.ewm.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.location.LocationDto;
import ru.practicum.ewm.mainservice.util.Marker;
import ru.practicum.ewm.mainservice.validation.FutureDateTime;
import ru.practicum.mainservice.constants.Variables;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEventDto {

    @Size(min = 20,
          max = 2000,
          groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreation.class)
    private String annotation;

    @JsonFormat(pattern = Variables.DATE_FORMAT)
    @NotNull(groups = Marker.OnCreation.class)
    @FutureDateTime(hours = 2,
                    message = "Date must be at least 2 hours in the future",
                    groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    private LocalDateTime eventDate;

    @NotNull(groups = Marker.OnCreation.class,
             message = Variables.MUST_NOT_BE_BLANK)
    private LocationDto location;

    private Boolean paid;

    @Size(min = 3,
          max = 120,
          groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreation.class,
              message = Variables.MUST_NOT_BE_BLANK)
    private String title;
}