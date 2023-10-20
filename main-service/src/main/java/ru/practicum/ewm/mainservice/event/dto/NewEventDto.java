package ru.practicum.ewm.mainservice.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.util.Marker;
import ru.practicum.ewm.stats.dto.Variables;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Setter
@Getter
public class NewEventDto extends BaseEventDto {
    @Positive(groups = Marker.OnCreation.class)
    @Positive(groups = Marker.OnCreation.class)
    private Long category;

    @NotBlank(groups = Marker.OnCreation.class, message = Variables.MUST_NOT_BE_BLANK)
    @Size(min = 20, max = 7000, groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreation.class, message = Variables.MUST_NOT_BE_BLANK)
    @Size(min = 20, max = 7000, groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    private String description;

    private Integer participantLimit;

    private Boolean requestModeration;

}