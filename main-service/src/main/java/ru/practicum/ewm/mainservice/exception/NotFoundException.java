package ru.practicum.ewm.mainservice.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}