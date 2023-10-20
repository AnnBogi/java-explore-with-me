package ru.practicum.ewm.mainservice.event.dto.request_param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.stats.dto.Variables;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public abstract class RequestParameters {

    private List<Long> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime rangeStart;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    private LocalDateTime rangeEnd;

    @Min(0L)
    private int from = 0;

    @Min(1L)
    @Max(1000L)
    private int size = 10;

    @AssertTrue
    @JsonIgnore
    public boolean isValidateRangeDate() {
        if (rangeStart == null || rangeEnd == null) {
            return true;
        }
        return rangeEnd.isAfter(rangeStart);
    }
}