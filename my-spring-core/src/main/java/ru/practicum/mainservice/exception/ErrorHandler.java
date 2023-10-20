package ru.practicum.mainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @ExceptionHandler
    public ResponseEntity<ApiError> handler(Throwable e) {
        String reason = MessageFormat.format("Unknown error on server: {0}", e.getClass());
        ApiError error = createError(e, reason, HttpStatus.INTERNAL_SERVER_ERROR);
        return createResponse(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handler(MissingServletRequestParameterException e) {
        String reason = "Incorrectly made request.";
        ApiError error = createError(e, reason, HttpStatus.BAD_REQUEST);
        return createResponse(error);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiError> onConstraintValidationException(Exception e) {
        String reason = "Validation error";
        ApiError error = createError(e, reason, HttpStatus.BAD_REQUEST);
        return createResponse(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> onConditionNotMetException(ConditionNotMetException e) {
        String reason = "For the requested operation the conditions are not met.";
        ApiError error = createError(e, reason, HttpStatus.CONFLICT);
        return createResponse(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> onNumberFormatException(NumberFormatException e) {
        String reason = "Incorrectly made request.";
        ApiError error = createError(e, reason, HttpStatus.BAD_REQUEST);
        return createResponse(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> onNotFoundException(NotFoundException e) {
        String reason = "The required object was not found.";
        ApiError error = createError(e, reason, HttpStatus.NOT_FOUND);
        return createResponse(error);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String reason = "Integrity constraint has been violated.";
        ApiError error = createError(e, reason, HttpStatus.CONFLICT);
        return createResponse(error);
    }

    private static ApiError createError(Throwable e, String reason, HttpStatus status) {
        log.info(e.getMessage());
        log.info(reason, e);
        return new ApiError()
                .setMessage(e.getMessage())
                .setReason(reason)
                .setStatus(status)
                .setTimestamp(LocalDateTime.now().format(FORMATTER));
    }

    private ResponseEntity<ApiError> createResponse(ApiError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}