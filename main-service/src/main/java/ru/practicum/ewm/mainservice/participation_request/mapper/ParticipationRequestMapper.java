package ru.practicum.ewm.mainservice.participation_request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.mainservice.entity.ParticipationRequest;
import ru.practicum.ewm.mainservice.participation_request.dto.ParticipationRequestDto;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toDto(ParticipationRequest participationRequest);
}