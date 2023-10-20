package ru.practicum.ewm.mainservice.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.UserShortDto;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;

@Getter
@Setter
public abstract class ResponseEventDto extends BaseEventDto {
    private CategoryDto category;
    private long confirmedRequests;
    private long id;
    private UserShortDto initiator;
    private long views;
}