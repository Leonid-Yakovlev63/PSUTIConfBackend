package ru.psuti.conf.dto.response.users;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.psuti.conf.dto.response.CompactConferenceDTO;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserConference extends CompactConferenceDTO {
    Integer permissions;

    public UserConference(ConferenceUserPermissions conferenceUserPermissions) {
        super(conferenceUserPermissions.getConference());
        this.permissions = conferenceUserPermissions.getPermissions();
    }
}
