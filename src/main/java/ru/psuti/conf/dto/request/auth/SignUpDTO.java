package ru.psuti.conf.dto.request.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.validation.anotation.PreferredLocaleInNames;

import java.util.Map;

@Data
@PreferredLocaleInNames
public class SignUpDTO {
    @Email
    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotNull
    private Locale preferredLocale;

    @Valid
    private Map<Locale, UserNamesDTO> names;
}
