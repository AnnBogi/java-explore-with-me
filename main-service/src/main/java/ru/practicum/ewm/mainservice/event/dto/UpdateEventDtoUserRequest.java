package ru.practicum.ewm.mainservice.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventDtoUserRequest extends NewEventDto {

    private UserUpdateEventState stateAction;
}