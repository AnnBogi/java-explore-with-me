package ru.practicum.ewm.mainservice.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.mainservice.util.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public abstract class BaseCompilationDto {
    @NotBlank(groups = Marker.OnCreation.class)
    @Size(max = 50, groups = {Marker.OnCreation.class, Marker.OnUpdate.class})
    private String title;

    private Boolean pinned;
}