package ru.practicum.ewm.mainservice.exception;

import java.text.MessageFormat;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String s) {
        super(s);
    }

    public AccessDeniedException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}