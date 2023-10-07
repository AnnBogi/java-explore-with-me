package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.AppName;
import ru.practicum.ewm.stats.dto.ViewStat;
import ru.practicum.ewm.stats.dto.ViewStatsRequest;
import ru.practicum.ewm.stats.server.entity.EndpointHit;
import ru.practicum.ewm.stats.server.repository.StatsRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsService {

    private final StatsRepository statsRepository;

    public EndpointHit save(EndpointHit endpointHit) {
        return statsRepository.save(endpointHit);
    }

    public List<ViewStat> getStats(ViewStatsRequest request) {
        List<Object[]> result;
        if (request.isUnique()) {
            result = statsRepository.countDistinctIpByUris(request.getUris(), request.getStart(), request.getEnd());
        } else {
            result = statsRepository.countIpByUris(request.getUris(), request.getStart(), request.getEnd());
        }
        return result.stream()
                .map(this::getViewStat)
                .collect(Collectors.toList());
    }

    private ViewStat getViewStat(Object[] objects) {
        return new ViewStat()
                .setApp(AppName.EWM_MAIN_SERVICE)
                .setUri(objects[0].toString())
                .setHits((Long) objects[1]);
    }
}