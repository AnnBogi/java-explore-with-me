package ru.practicum.mainservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ApiError {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors;
    private HttpStatus status;
    private String reason;
    private String message;
    private String timestamp;
}