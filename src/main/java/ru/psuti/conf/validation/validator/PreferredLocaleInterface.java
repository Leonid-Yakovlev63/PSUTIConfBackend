package ru.psuti.conf.validation.validator;

import ru.psuti.conf.dto.request.auth.UserNamesDTO;
import ru.psuti.conf.entity.Locale;

import java.util.Map;

public interface PreferredLocaleInterface {
    Map<Locale, UserNamesDTO> getNames();

    Locale getPreferredLocale();
}
