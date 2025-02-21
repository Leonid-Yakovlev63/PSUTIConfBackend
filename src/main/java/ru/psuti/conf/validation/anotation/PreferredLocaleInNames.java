package ru.psuti.conf.validation.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.psuti.conf.validation.validator.PreferredLocaleInNamesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PreferredLocaleInNamesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferredLocaleInNames {
    String message() default "Names must contain the preferred locale";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
