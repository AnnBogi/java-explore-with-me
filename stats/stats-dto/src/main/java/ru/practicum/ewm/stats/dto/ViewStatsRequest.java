package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.constants.Variables;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class ViewStatsRequest {

    @NotNull
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    LocalDateTime start;

    @NotNull
    @DateTimeFormat(pattern = Variables.DATE_FORMAT)
    LocalDateTime end;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> uris;

    boolean unique;

    @JsonIgnore
    @AssertTrue(message = "End date must be after start date and dates cannot be equal")
    public boolean isValidDate() {
        if (start == null || end == null) {
            return false;
        }
        return end.isAfter(start) && !end.equals(start);
    }
}