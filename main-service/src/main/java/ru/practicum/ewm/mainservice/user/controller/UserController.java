package ru.practicum.ewm.mainservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.mainservice.event.dto.NewEventDto;
import ru.practicum.ewm.mainservice.event.dto.ResponseEventDto;
import ru.practicum.ewm.mainservice.event.dto.UpdateEventDtoUserRequest;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.service.EventService;
import ru.practicum.ewm.mainservice.participation_request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.mainservice.participation_request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.mainservice.participation_request.dto.ParticipationRequestDto;
import ru.practicum.ewm.mainservice.participation_request.entity.ParticipationRequest;
import ru.practicum.ewm.mainservice.participation_request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.mainservice.participation_request.service.ParticipationRequestService;
import ru.practicum.ewm.mainservice.user.service.UserService;
import ru.practicum.ewm.mainservice.util.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    public final EventMapper eventMapper;
    public final ParticipationRequestMapper participationRequestMapper;
    public final UserService userService;
    public final EventService eventService;
    public final ParticipationRequestService participationRequestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequests(@PathVariable @Positive long userId,
                                                  @RequestParam @Positive long eventId) {
        log.info("Create a participation request for event with ID {} from user with ID {}", eventId, userId);
        ParticipationRequest request = participationRequestService.createRequest(userId, eventId);
        return participationRequestMapper.toDto(request);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestByUserId(@PathVariable @Positive long userId) {
        log.info("Get a participation request by user with ID {}", userId);
        List<ParticipationRequest> requests = participationRequestService.getRequestByUserId(userId);
        return getRequestDtoList(requests);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Positive long userId,
                                                              @PathVariable @Positive long requestId) {
        log.info("Cancel participation request with ID={} from user with ID={}", requestId, userId);
        return participationRequestMapper.toDto(
                participationRequestService.cancelRequestFromRequester(requestId, userId)
        );
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable @Positive long userId,
                                                                  @PathVariable @Positive long eventId) {
        log.info("Get requests of participants in event with ID={} for initiator with ID={}", eventId, userId);
        List<ParticipationRequest> requests = participationRequestService.getRequestsForInitiator(userId, eventId);
        return getRequestDtoList(requests);

    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchParticipationRequests(@PathVariable @Positive long userId,
                                                                     @PathVariable @Positive long eventId,
                                                                     @Valid @RequestBody
                                                                     EventRequestStatusUpdateRequest request) {
        log.info("Patch requests of participants in event with ID={} from initiator with ID={}", eventId, userId);
        Map<Boolean, List<ParticipationRequest>> requests = participationRequestService
                .updateRequestsFromInitiator(userId, eventId, request);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(requests.get(true).stream()
                .map(participationRequestMapper::toDto)
                .collect(Collectors.toList()));
        result.setRejectedRequests(requests.get(false).stream()
                .map(participationRequestMapper::toDto).collect(Collectors.toList()));
        return result;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public ResponseEventDto createEvent(@PathVariable @Positive long userId,
                                        @RequestBody @Validated(Marker.OnCreation.class) NewEventDto eventDto) {
        log.info("Create event from user ID: {} - {}", userId, eventDto);
        setDefaultValue(eventDto);
        Event event = eventService.save(eventMapper.fromRequest(eventDto, userId));
        return eventMapper.toFullDto(event);
    }

    @GetMapping("/{userId}/events")
    public List<ResponseEventDto> readEventsByUserId(@PathVariable @Positive long userId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get events for user id: {}", userId);
        List<Event> eventsByInitiator = eventService.findEventsByInitiator(userId, from, size);
        return eventsByInitiator
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEventDto getEventByInitiator(@PathVariable @Positive long userId,
                                                @PathVariable @Positive long eventId) {
        log.info("Get event with ID: {}. From user ID: {}", eventId, userId);
        Event event = eventService.getEventByInitiator(eventId, userId);
        return eventMapper.toFullDto(event);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEventDto updateEvents(@PathVariable @Positive long userId,
                                         @PathVariable @Positive long eventId,
                                         @Validated(Marker.OnUpdate.class)
                                         @RequestBody UpdateEventDtoUserRequest eventUserRequest) {
        log.info("Update event with ID: {}. From user ID: {}", eventId, userId);
        Event event = eventMapper.fromUpdateUserRequest(eventUserRequest, eventId);
        event = eventService.updateEventByUser(event, userId);
        return eventMapper.toFullDto(event);
    }

    private void setDefaultValue(NewEventDto eventDto) {
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }
    }

    private List<ParticipationRequestDto> getRequestDtoList(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(participationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}