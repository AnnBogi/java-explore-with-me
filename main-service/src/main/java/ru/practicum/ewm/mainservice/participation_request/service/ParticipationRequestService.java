package ru.practicum.ewm.mainservice.participation_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mainservice.event.State;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;
import ru.practicum.ewm.mainservice.participation_request.Status;
import ru.practicum.ewm.mainservice.participation_request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.mainservice.participation_request.entity.ParticipationRequest;
import ru.practicum.ewm.mainservice.participation_request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.mainservice.user.entity.User;
import ru.practicum.ewm.mainservice.user.repository.UserRepository;
import ru.practicum.mainservice.exception.ConditionNotMetException;
import ru.practicum.mainservice.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ParticipationRequest createRequest(long userId, long eventId) {

        Event event = getEventOrThrowException(userId, eventId);

        User requester = userRepository.findByIdOrThrowNotFoundException(userId, "User");
        ParticipationRequest participationRequest = new ParticipationRequest()
                .setCreated(LocalDateTime.now())
                .setRequester(requester)
                .setEvent(event);

        if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
            participationRequest.setStatus(Status.PENDING);
        } else {
            int countParticipants = event.getConfirmedRequests();
            if (countParticipants != 0 && countParticipants >= event.getParticipantLimit()) {
                throw new ConditionNotMetException("The requested action cannot be completed due to reaching " +
                        "the participation limit. Limit: {0}", event.getParticipantLimit());
            }
            participationRequest.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(countParticipants + 1);
        }


        participationRequest = requestRepository.save(participationRequest);

        return participationRequest;
    }

    public List<ParticipationRequest> readRequestByUserId(long userId) {
        User requester = userRepository.findByIdOrThrowNotFoundException(userId, "User");
        return requestRepository.findAllByRequester(requester);
    }

    public ParticipationRequest cancelRequestFromRequester(long requestId, long requesterId) {
        ParticipationRequest request = requestRepository
                .findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(() -> new NotFoundException("Request with id={0} was not found", requestId));
        request.setStatus(Status.CANCELED);
        return requestRepository.save(request);
    }

    public List<ParticipationRequest> getRequestsForInitiator(long userId, long eventId) {
        return requestRepository.findByInitiatorIdAndEventId(userId, eventId);
    }

    public Map<Boolean, List<ParticipationRequest>> updateRequestsFromInitiator(long userId, long eventId,
                                                                                EventRequestStatusUpdateRequest updateStatusRequest) {
        List<ParticipationRequest> requests = requestRepository
                .findRequests(userId, eventId, updateStatusRequest.getRequestIds());
        if (requests.isEmpty()) {
            throw new NotFoundException("Request from event with id={0} was not found", eventId);
        }
        Event event = requests.get(0).getEvent();
        AtomicInteger limit = new AtomicInteger(event.getParticipantLimit() - event.getConfirmedRequests());
        if (limit.get() <= 0) {
            throw new ConditionNotMetException("The participant limit has been reached");
        }
        Map<Boolean, List<ParticipationRequest>> considerRequest = consedRequests(updateStatusRequest, requests, limit);
        requestRepository.saveAll(considerRequest.get(true));
        requestRepository.saveAll(considerRequest.get(false));
        event.setConfirmedRequests(event.getConfirmedRequests() + considerRequest.get(true).size());
        eventRepository.save(event);
        return considerRequest;
    }

    private Map<Boolean, List<ParticipationRequest>> consedRequests(
        EventRequestStatusUpdateRequest updateStatusRequest,
        List<ParticipationRequest> requests,
        AtomicInteger limit) {

        return requests.stream()
                .map(request -> {
                    if (request.getStatus() == Status.CONFIRMED) {
                        throw new ConditionNotMetException("");
                    }
                    if (updateStatusRequest.getStatus() == EventRequestStatusUpdateRequest.Status.REJECTED) {
                        request.setStatus(Status.REJECTED);
                    } else {
                        if (limit.getAndDecrement() > 0) {
                            request.setStatus(Status.CONFIRMED);
                        } else {
                            request.setStatus(Status.REJECTED);
                        }
                    }
                    return request;
                })
                .collect(Collectors.partitioningBy(request -> request.getStatus() == Status.CONFIRMED));
    }

    private Event getEventOrThrowException(long userId, long eventId) {
        Event event = eventRepository.findByIdOrThrowNotFoundException(eventId, "Event");

        if (event.getInitiator().getId() == userId) {
            throw new ConditionNotMetException("The initiator cannot add a request to participate in his event");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ConditionNotMetException("You cannot participate in an unpublished event. Event state: {0}",
                    event.getState());
        }
        return event;
    }
}