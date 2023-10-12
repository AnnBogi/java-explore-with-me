package ru.practicum.ewm.mainservice.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto extends BaseUser {

    @Pattern(regexp = "^[A-Z0-9._%+-]{1,64}@[A-Z0-9-]{1,63}(\\.[A-Z0-9-]{1,63})*\\.[A-Z0-9-]{2,63}$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "email is wrong")
    @NotBlank
    @Size(max = 254)
    private String email;
}