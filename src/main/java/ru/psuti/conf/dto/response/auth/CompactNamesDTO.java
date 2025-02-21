package ru.psuti.conf.dto.response.auth;

import lombok.Value;
import ru.psuti.conf.entity.auth.UserLocalized;

@Value
public class CompactNamesDTO {
    String firstName;
    String lastName;
    String middleName;

    public CompactNamesDTO(UserLocalized userLocalized) {
        this.firstName = userLocalized.getFirstName();
        this.lastName = userLocalized.getLastName();
        this.middleName = userLocalized.getMiddleName();
    }
}
