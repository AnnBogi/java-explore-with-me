package ru.practicum.ewm.mainservice.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.stats.dto.Variables;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public abstract class BaseUser {
    private long id;

    @NotBlank(message = Variables.MUST_NOT_BE_BLANK)
    @Size(min = 2, max = 250)
    private String name;
}