package ru.practicum.ewm.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mainservice.category.entity.Category;
import ru.practicum.ewm.mainservice.category.repository.CategoryRepository;
import ru.practicum.ewm.mainservice.event.State;
import ru.practicum.ewm.mainservice.event.dto.request_param.AdminEventRequestParameters;
import ru.practicum.ewm.mainservice.event.dto.request_param.EventRequestParameters;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;
import ru.practicum.ewm.mainservice.event.repository.EventRepositoryCustom;
import ru.practicum.ewm.mainservice.location.Location;
import ru.practicum.ewm.mainservice.location.LocationRepository;
import ru.practicum.ewm.mainservice.user.entity.User;
import ru.practicum.ewm.mainservice.user.repository.UserRepository;
import ru.practicum.mainservice.exception.AccessDeniedException;
import ru.practicum.mainservice.exception.ConditionNotMetException;
import ru.practicum.mainservice.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventRepositoryCustom eventRepositoryCustom;
    private final LocationRepository locationRepository;
    @Qualifier("modelMapperEventService")
    private final ModelMapper modelMapper;

    @Override
    public Event save(Event event) {
        if (event.getInitiator() == null) {
            throw new NotFoundException("User not found");
        }
        if (event.getCategory() == null) {
            throw new NotFoundException("Category not found");
        }
        User user = userRepository.findByIdOrThrowNotFoundException(event.getInitiator().getId(), "User");
        Category category = categoryRepository.findByIdOrThrowNotFoundException(event.getCategory().getId(), "Category");
        event.setLocation(locationRepository.save(event.getLocation()));
        event.setInitiator(user)
                .setCategory(category)
                .setCreatedOn(LocalDateTime.now())
                .setState(State.PENDING);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findEventsByInitiator(long userId, int from, int size) {
        return eventRepositoryCustom.findEventsByInitiatorId(userId, from, size);
    }

    @Override
    public Event updateEventByUser(Event updateEvent, long userId) {
        Event event = eventRepository.findByIdOrThrowNotFoundException(updateEvent.getId(), "Event");
        if (event.getState() == State.PUBLISHED) {
            throw new ConditionNotMetException("Only pending or canceled events can be changed");
        }
        User initiator = userRepository.findByIdOrThrowNotFoundException(userId, "User");
        if (event.getInitiator().getId() != initiator.getId()) {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
        return updateEvent(updateEvent, event);
    }

    @Override
    public Event updateEventByAdmin(Event updateEvent) {
        Event event = eventRepository.findByIdOrThrowNotFoundException(updateEvent.getId(), "Event");
        return updateEvent(updateEvent, event);
    }

    @Override
    public List<Event> findEventsForAdmin(AdminEventRequestParameters adminEventRequestParameters) {
        return eventRepositoryCustom.findEventsByParameters(adminEventRequestParameters);
    }

    @Override
    public List<Event> findEventsForAdmin(EventRequestParameters eventRequestParameters) {
        return eventRepositoryCustom.findEventsByParameters(eventRequestParameters);
    }

    @Override
    public Event findPublishedEventById(long eventId) {
        return eventRepositoryCustom.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with ID = {0} not found", eventId));
    }

    @Override
    public void updateEventViews(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Event getEventByInitiator(long eventId, long initiatorId) {
        return eventRepositoryCustom.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException("Event with ID = {0} and initiator ID = {1} not found",
                        eventId, initiatorId));
    }

    @Override
    public List<Event> getPublicEvents(EventRequestParameters eventRequestParameters) {
        return eventRepositoryCustom.findEventsByParameters(eventRequestParameters);
    }

    private Event updateEvent(Event updateEvent, Event event) {
        throwIfNotRightState(updateEvent, event);
        if (updateEvent.getCategory() != null) {
            if (updateEvent.getCategory().getId() != event.getCategory().getId()) {
                Category category = categoryRepository.findByIdOrThrowNotFoundException(event.getCategory().getId(), "Category");
                updateEvent.setCategory(category);
            } else {
                updateEvent.setCategory(event.getCategory());
            }
        }
        Location updateLocation = updateEvent.getLocation();
        Location location = event.getLocation();
        if (updateLocation != null && updateLocation != location) {
            event.setLocation(locationRepository.save(updateLocation));
        }

        modelMapper.map(updateEvent, event);
        if (event.getState() == State.PUBLISHED) {
            event.setPublishedOn(LocalDateTime.now());
        }

        return eventRepository.save(event);
    }

    private static void throwIfNotRightState(Event updateEvent, Event event) {
        if (updateEvent.getState() == State.PUBLISHED && event.getState() != State.PENDING) {
            throw new ConditionNotMetException("Cannot publish the event because it's not in the right state: {0}",
                    event.getState());
        }
        if (updateEvent.getState() == State.CANCELED && event.getState() == State.PUBLISHED) {
            throw new ConditionNotMetException("Cannot  canceled  the event because it is PUBLISHED");
        }
    }
}