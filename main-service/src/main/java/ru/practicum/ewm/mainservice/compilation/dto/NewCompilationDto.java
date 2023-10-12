package ru.practicum.ewm.mainservice.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class NewCompilationDto extends BaseCompilationDto {
    private List<Long> events;
}