package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AppName {
    EWM_MAIN_SERVICE("ewm-main-service");

    private final String value;

    AppName(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AppName fromValue(String value) {
        for (AppName appName : AppName.values()) {
            if (appName.value.equalsIgnoreCase(value)) {
                return appName;
            }
        }
        throw new IllegalArgumentException("Invalid value for AppName: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}