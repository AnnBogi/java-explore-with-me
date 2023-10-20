package ru.practicum.ewm.mainservice.event.dto.request_param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventRequestParameters extends RequestParameters {
    private String text;

    private boolean paid;

    private boolean onlyAvailable;

    private Sort sort;
}