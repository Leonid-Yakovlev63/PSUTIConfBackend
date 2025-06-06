package ru.psuti.conf.dto.response.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.psuti.conf.dto.response.auth.CompactNamesDTO;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.entity.auth.UserLocalized;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class FullUserDTO {
    UUID id;

    String email;

    Boolean emailVerified;

    Role role;

    String psutiUsername;

    Locale preferredLocale;

    Map<Locale, CompactNamesDTO> names;

    String country;

    String city;

    String organization;

    String organizationAddress;

    LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<UserConference> conferences;

    public FullUserDTO(User user) {
        this(user, false);
    }

    public FullUserDTO(User user, boolean includeConferences) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.emailVerified = user.getEmailVerified();
        this.role = user.getRole();
        this.psutiUsername = user.getPsutiUsername();
        this.preferredLocale = user.getPreferredLocale();
        this.names = user.getNames().stream()
                .collect(Collectors.toMap(
                        UserLocalized::getLocale,
                        CompactNamesDTO::new
                ));
        this.country = user.getCountry();
        this.city = user.getCity();
        this.organization = user.getOrganization();
        this.organizationAddress = user.getOrganizationAddress();
        this.createdAt = user.getCreatedAt();

        if (includeConferences) {
            this.conferences = user.getConferenceUserPermissions().stream()
                    .limit(3).map(UserConference::new).toList();
        } else {
            this.conferences = null;
        }
    }

}
