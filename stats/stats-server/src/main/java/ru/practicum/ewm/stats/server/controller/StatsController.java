package ru.practicum.ewm.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStat;
import ru.practicum.ewm.stats.dto.ViewStatsRequest;
import ru.practicum.ewm.stats.server.EndpointHitMapper;
import ru.practicum.ewm.stats.server.entity.EndpointHit;
import ru.practicum.ewm.stats.server.service.StatsService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final EndpointHitMapper endpointHitMapper;
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto hit(@RequestBody EndpointHitDto hitDto) {
        EndpointHit hit = statsService.save(endpointHitMapper.fromDto(hitDto));
        return endpointHitMapper.toDto(hit);
    }

    @GetMapping("/stats")
    public List<ViewStat> viewStat(ViewStatsRequest viewStatsRequest) {
        return statsService.getStats(viewStatsRequest);
    }
}