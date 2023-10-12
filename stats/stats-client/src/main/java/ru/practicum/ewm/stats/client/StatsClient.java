package ru.practicum.ewm.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.dto.AppName;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.Variables;
import ru.practicum.ewm.stats.dto.ViewStatsRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class StatsClient extends BaseClient {
    private final AppName appName;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, @Value("${app-name}") String appName,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.appName = AppName.fromValue(appName);

    }

    public void hit(HttpServletRequest request) {
        String uri = request.getRequestURI().replaceAll("/$", "");
        EndpointHitDto endpointHitDto = new EndpointHitDto()
                .setApp(appName)
                .setIp(request.getRemoteAddr())
                .setUri(uri)
                .setTimestamp(LocalDateTime.now());
        post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> stats(ViewStatsRequest viewStatsRequest) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", viewStatsRequest.getStart().format(DateTimeFormatter.ofPattern(Variables.DATE_FORMAT)));
        param.put("end", viewStatsRequest.getEnd().format(DateTimeFormatter.ofPattern(Variables.DATE_FORMAT)));
        param.put("unique", viewStatsRequest.isUnique());
        String path = "/stats?start={start}&end={end}&unique={unique}";
        if (viewStatsRequest.getUris() != null && !viewStatsRequest.getUris().isEmpty()) {
            param.put("uris", String.join(",",  viewStatsRequest.getUris()));
            path += "&uris={uris}";
        }
        return get(path, param);
    }
}