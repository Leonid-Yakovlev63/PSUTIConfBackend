package ru.psuti.conf.validation.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.psuti.conf.validation.validator.ConferenceNameEnValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConferenceNameEnValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConferenceNameEnRequiredIfEnglishEnabled {
    String message() default "Conference name in English must not be empty if English is enabled";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
