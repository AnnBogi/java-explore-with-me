package ru.practicum.ewm.mainservice.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.user.dto.UserShortDto;

@Getter
@Setter
public abstract class ResponseEventDto extends BaseEventDto {
    private CategoryDto category;
    private long confirmedRequests;
    private long id;
    private UserShortDto initiator;
    private long views;
}