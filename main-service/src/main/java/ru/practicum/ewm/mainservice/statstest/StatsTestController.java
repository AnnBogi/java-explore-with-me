package ru.practicum.ewm.mainservice.statstest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.ViewStatsRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsTestController {

    private final StatsClient statsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(ViewStatsRequest viewStatsRequest) {
        return statsClient.stats(viewStatsRequest);
    }

    @PostMapping("/hit")
    public void logIPAndPath(HttpServletRequest request) {
        statsClient.hit(request);
    }
}