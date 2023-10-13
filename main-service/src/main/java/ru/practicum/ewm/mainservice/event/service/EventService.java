package ru.practicum.ewm.mainservice.event.service;

import ru.practicum.ewm.mainservice.event.dto.request_param.AdminEventRequestParameters;
import ru.practicum.ewm.mainservice.event.dto.request_param.EventRequestParameters;
import ru.practicum.ewm.mainservice.event.entity.Event;

import java.util.List;

public interface EventService {
    Event save(Event event);

    List<Event> findEventsByInitiator(long userId, int from, int size);

    Event updateEventByUser(Event updateEvent, long userId);

    Event updateEventByAdmin(Event updateEvent);

    List<Event> findEventsForAdmin(AdminEventRequestParameters adminEventRequestParameters);

    List<Event> findEventsForAdmin(EventRequestParameters eventRequestParameters);

    Event findPublishedEventById(long eventId);

    void updateEventViews(Event event);

    Event getEventByInitiator(long eventId, long userId);

    List<Event> getPublicEvents(EventRequestParameters eventRequestParameters);
}