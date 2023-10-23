package ru.practicum.ewm.mainservice.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.mainservice.category.entity.Category;
import ru.practicum.ewm.mainservice.event.dto.AdminUpdateEventState;
import ru.practicum.ewm.mainservice.event.dto.EventFullDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.dto.NewEventDto;
import ru.practicum.ewm.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.mainservice.event.dto.UpdateEventDtoUserRequest;
import ru.practicum.ewm.mainservice.event.dto.UserUpdateEventState;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.entity.State;
import ru.practicum.ewm.mainservice.user.entity.User;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "views", constant = "0L")
    @Mapping(target = "initiator", source = "userId")
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    Event fromRequest(NewEventDto newEventDto, long userId);

    @Mapping(target = "views", ignore = true)
    @Mapping(target = "state", source = "updateEventDto.stateAction")
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    Event fromUpdateUserRequest(UpdateEventDtoUserRequest updateEventDto, long id);

    @Mapping(target = "views", ignore = true)
    @Mapping(target = "state", source = "updateEventDto.stateAction")
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    Event fromUpdateAdminRequest(UpdateEventAdminRequest updateEventDto, long id);

    EventFullDto toFullDto(Event event);

    @Mapping(target = "commentsCount", source = "commentCount")
    EventShortDto toShortDto(Event event, long commentCount);

    EventShortDto toShortDto(Event event);

    default Category map(long value) {
        if (value == 0) {
            return null;
        }
        return new Category().setId(value);
    }

    default User mapInitiator(long value) {
        return new User().setId(value);
    }

    default State mapState(UserUpdateEventState userUpdateEventState) {
        if (userUpdateEventState == null) {
            return null;
        }
        switch (userUpdateEventState) {
            case SEND_TO_REVIEW:
                return State.PENDING;
            case CANCEL_REVIEW:
                return State.CANCELED;
            default:
                throw new IllegalArgumentException("Fail state");
        }
    }

    default State mapState(AdminUpdateEventState adminUpdateEventState) {
        if (adminUpdateEventState == null) {
            return null;
        }
        switch (adminUpdateEventState) {
            case PUBLISH_EVENT:
                return State.PUBLISHED;
            case REJECT_EVENT:
                return State.CANCELED;
            default:
                throw new IllegalArgumentException("Fail state");
        }
    }
}