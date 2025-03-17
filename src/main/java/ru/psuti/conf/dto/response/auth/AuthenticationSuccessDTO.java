package ru.psuti.conf.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationSuccessDTO {
    private String accessToken;
}
