package ru.practicum.ewm.mainservice.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
public class CompilationDto extends BaseCompilationDto {
    private long id;
    private List<EventShortDto> events;
}