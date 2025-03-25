package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AddAdminForConferenceDTO {

    @Email
    @NotBlank
    String email;

    @NotNull
    Integer permissions;

}
