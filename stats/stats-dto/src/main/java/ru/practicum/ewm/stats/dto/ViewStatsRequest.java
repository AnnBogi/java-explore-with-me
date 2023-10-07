package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class ViewStatsRequest {

    @NotNull
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime end;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> uris;

    private boolean unique;
}