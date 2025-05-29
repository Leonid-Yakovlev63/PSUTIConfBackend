package ru.psuti.conf.dto.response;

import lombok.Builder;
import lombok.Value;
import ru.psuti.conf.dto.response.auth.CompactNamesDTO;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;
import ru.psuti.conf.entity.auth.Role;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class ConferenceAdminDTO {

    UUID id;

    String email;

    Map<Locale, CompactNamesDTO> names;

    Integer permissions;

}
