package ru.practicum.ewm.stats.server;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.server.entity.EndpointHit;

@Component
public class EndpointHitMapper {
    public EndpointHit fromDto(EndpointHitDto hit) {
        return new EndpointHit()
                .setApp(hit.getApp())
                .setUri(hit.getUri())
                .setTimestamp(hit.getTimestamp())
                .setIp(hit.getIp());
    }

    public EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto()
                .setId(hit.getId())
                .setApp(hit.getApp())
                .setUri(hit.getUri())
                .setTimestamp(hit.getTimestamp())
                .setIp(hit.getIp());
    }
}