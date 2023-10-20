package ru.practicum.ewm.mainservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.mainservice.user.dto.UserDto;
import ru.practicum.ewm.mainservice.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User fromRequest(UserDto userDto);

    UserDto toDto(User user);
}