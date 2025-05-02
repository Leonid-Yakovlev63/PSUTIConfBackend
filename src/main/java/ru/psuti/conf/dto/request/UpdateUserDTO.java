package ru.psuti.conf.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import ru.psuti.conf.dto.request.auth.UserNamesDTO;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.validation.anotation.PreferredLocaleInNames;
import ru.psuti.conf.validation.validator.PreferredLocaleInterface;

import java.util.Map;

@Data
@PreferredLocaleInNames
public class UpdateUserDTO implements PreferredLocaleInterface {

    @Size(max = 300, message = "Страна должна быть до 300 символов")
    String country;

    @Size(max = 300, message = "Город должен быть до 300 символов")
    String city;

    @Size(max = 900, message = "Организация должна быть до 900 символов")
    String organization;

    @Size(max = 450, message = "Адрес организации должен быть до 450 символов")
    String organizationAddress;

    @NotNull
    Locale preferredLocale;

    @Valid
    Map<Locale, UserNamesDTO> names;
}
