package ru.psuti.conf.dto.response.auth;

import lombok.Value;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.entity.auth.UserLocalized;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO for {@link User}
 */
@Value
public class CompactUserDTO {
    UUID id;

    String email;

    Role role;

    String psutiUsername;

    Locale preferredLocale;

    Map<Locale, CompactNamesDTO> names;

    List<ConferencePermissionsDTO> conferences;

    public CompactUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.psutiUsername = user.getPsutiUsername();
        this.preferredLocale = user.getPreferredLocale();
        this.names = user.getNames().stream()
                .collect(Collectors.toMap(
                        UserLocalized::getLocale,
                        CompactNamesDTO::new
                ));
        if (role.equals(Role.ADMIN)) {
            this.conferences = user.getConferenceUserPermissions().stream()
                    .map(ConferencePermissionsDTO::new).toList();
        } else {
            this.conferences = null;
        }
    }
}