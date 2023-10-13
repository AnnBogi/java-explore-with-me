package ru.practicum.ewm.mainservice.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainservice.compilation.dto.UpdateCompilationDTO;
import ru.practicum.ewm.mainservice.compilation.entity.Compilation;
import ru.practicum.ewm.mainservice.event.entity.Event;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    Compilation fromDto(CompilationDto compilationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", qualifiedByName = "mapForNew")
    Compilation fromDto(NewCompilationDto compilationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", qualifiedByName = "mapForUpdate")
    Compilation fromDto(UpdateCompilationDTO compilationDto);

    @Named("mapForNew")
    default List<Event> mapForNew(List<Long> value) {
        if (value == null) {
            return Collections.emptyList();
        }
        return map(value);
    }

    @Named("mapForUpdate")
    default List<Event> mapForUpdate(List<Long> value) {
        if (value == null) {
            return null;
        }
        return map(value);
    }

    default List<Event> map(List<Long> value) {
        return value.stream()
                .map(eventId -> new Event().setId(eventId))
                .collect(Collectors.toList());
    }
}