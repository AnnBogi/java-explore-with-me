package ru.practicum.ewm.mainservice.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureDateTimeValidator implements ConstraintValidator<FutureDateTime, LocalDateTime> {

    private int hours;

    @Override
    public void initialize(FutureDateTime annotation) {
        this.hours = annotation.hours();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime futureDateTime = currentDateTime.plusHours(hours);

        return value.isAfter(futureDateTime);
    }
}