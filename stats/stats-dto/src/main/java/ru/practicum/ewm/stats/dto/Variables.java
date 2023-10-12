package ru.practicum.ewm.stats.dto;

import java.time.format.DateTimeFormatter;

public final class Variables {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final String MUST_NOT_BE_BLANK = "must not be blank";


    private Variables() {
    }
}