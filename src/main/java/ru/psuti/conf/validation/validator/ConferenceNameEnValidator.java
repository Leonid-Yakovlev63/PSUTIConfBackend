package ru.psuti.conf.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.psuti.conf.dto.request.CreateConferenceDTO;
import ru.psuti.conf.validation.anotation.ConferenceNameEnRequiredIfEnglishEnabled;

public class ConferenceNameEnValidator implements ConstraintValidator<ConferenceNameEnRequiredIfEnglishEnabled, CreateConferenceDTO> {

    @Override
    public boolean isValid(CreateConferenceDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (Boolean.TRUE.equals(value.getIsEnglishEnabled())) {
            return value.getConferenceNameEn() != null && !value.getConferenceNameEn().isEmpty();
        }

        return true;
    }
}
