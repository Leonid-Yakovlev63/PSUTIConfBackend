package ru.psuti.conf.dto.response.auth;

import lombok.Value;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;

@Value
public class ConferencePermissionsDTO {
    Long id;
    String slug;
    Integer permissions;

    public ConferencePermissionsDTO(ConferenceUserPermissions permissions) {
        Conference conference = permissions.getConference();
        this.id = conference.getId();
        this.slug = conference.getSlug();
        this.permissions = permissions.getPermissions();
    }
}
