package ru.psuti.conf.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.psuti.conf.validation.anotation.PreferredLocaleInNames;

import java.util.Map;

public class PreferredLocaleInNamesValidator implements ConstraintValidator<PreferredLocaleInNames, PreferredLocaleInterface> {

    @Override
    public boolean isValid(PreferredLocaleInterface dto, ConstraintValidatorContext context) {
        if (dto != null && dto.getNames() != null && dto.getPreferredLocale() != null) {
            if (dto.getNames().containsKey(dto.getPreferredLocale()))
                return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("names") // Указываем поле, которое вызвало ошибку
                .addConstraintViolation();
        return false;
    }
}
