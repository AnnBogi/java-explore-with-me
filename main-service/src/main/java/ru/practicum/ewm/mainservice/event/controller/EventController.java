package ru.practicum.ewm.mainservice.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.mainservice.event.dto.EventFullDto;
import ru.practicum.ewm.mainservice.event.dto.EventShortDto;
import ru.practicum.ewm.mainservice.event.dto.request_param.EventRequestParameters;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.service.EventService;
import ru.practicum.ewm.mainservice.exception.StatsRequestException;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.ViewStat;
import ru.practicum.ewm.stats.dto.ViewStatsRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(@Validated EventRequestParameters eventRequestParameters,
                                         HttpServletRequest request) {
        statsClient.hit(request);
        List<Event> events = eventService.getPublicEvents(eventRequestParameters);
        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable @Positive long eventId, HttpServletRequest request) throws JsonProcessingException {
        Event event = eventService.findPublishedEventById(eventId);

        String uri = request.getRequestURI().replaceAll("/$", "");
        long hits = getUniqueHits(uri);
        event.setViews(hits);
        eventService.updateEventViews(event);

        statsClient.hit(request);
        return eventMapper.toFullDto(event);
    }

    private long getUniqueHits(String uri) {
        List<ViewStat> viewStats = getViewStat(requestStatFromStatService(uri));
        return viewStats.stream().findFirst().map(ViewStat::getHits).orElse(0L);
    }

    private ResponseEntity<Object> requestStatFromStatService(String uri) {
        ViewStatsRequest viewStatsRequest = new ViewStatsRequest()
                .setStart(LocalDateTime.of(2001, 1, 1, 0, 0))
                .setEnd(LocalDateTime.now())
                .setUris(List.of(uri))
                .setUnique(true);
        return statsClient.stats(viewStatsRequest);
    }

    private List<ViewStat> getViewStat(ResponseEntity<Object> responseEntity) {
        StatsRequestException statsRequestException = new StatsRequestException("Failed to request statistics due to an error");
        if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getBody() == null) {
            throw statsRequestException;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
            return objectMapper.readValue(responseBody, new TypeReference<List<ViewStat>>() {
            });
        } catch (JsonProcessingException e) {
            throw statsRequestException;
        }
    }
}