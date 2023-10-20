package ru.practicum.ewm.mainservice.event.dto.request_param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.mainservice.event.State;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminEventRequestParameters extends RequestParameters {

    private List<Integer> users;

    private List<State> states;
}