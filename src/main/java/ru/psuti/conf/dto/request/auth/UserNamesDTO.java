package ru.psuti.conf.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class UserNamesDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    public String getFirstName() {
        return (firstName == null || firstName.isBlank()) ? null : firstName;
    }

    public String getLastName() {
        return (lastName == null || lastName.isBlank()) ? null : lastName;
    }

    public String getMiddleName() {
        return (middleName == null || middleName.isBlank()) ? null : middleName;
    }
}

