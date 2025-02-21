package ru.psuti.conf.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignIn {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
