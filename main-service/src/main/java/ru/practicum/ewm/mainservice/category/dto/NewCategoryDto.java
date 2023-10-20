package ru.practicum.ewm.mainservice.category.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.stats.dto.Variables;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewCategoryDto {

    @NotBlank(message = Variables.MUST_NOT_BE_BLANK)
    @Size(max = 50)
    private String name;
}