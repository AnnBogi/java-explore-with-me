package ru.practicum.ewm.mainservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDateTimeValidator.class)
public @interface FutureDateTime {

    String message() default "Date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hours() default 0;
}