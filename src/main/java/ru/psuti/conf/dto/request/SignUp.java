package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.psuti.conf.entity.User;

@Data
public class SignUp {
    @Email
    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String firstnameRu;

    @NotBlank
    @Size(max = 100)
    private String lastnameRu;

    @Size(max = 50)
    private String middlenameRu;

    public @Size(max = 50) String getMiddlenameRu() {
        if (middlenameRu == null || middlenameRu.isBlank())
            return null;
        return middlenameRu;
    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(getEmail())
                .password(passwordEncoder.encode(getPassword()))
                .firstnameRu(getFirstnameRu())
                .lastnameRu(getLastnameRu())
                .middlenameRu(getMiddlenameRu())
                .build();
    }
}
