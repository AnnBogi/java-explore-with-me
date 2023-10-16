package ru.practicum.ewm.mainservice.exception;

import java.text.MessageFormat;

public class ConditionNotMetException extends RuntimeException {
    public ConditionNotMetException(String message) {
        super(message);
    }

    public ConditionNotMetException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}