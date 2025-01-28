package ru.psuti.conf.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import ru.psuti.conf.entity.Role;
import ru.psuti.conf.entity.User;
import ru.psuti.conf.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitializer {
    private static final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;

    @Value("${files.upload-dir}")
    private String uploadDir;
    @Value("${admin.default.email}")
    private String adminEmail;
    @Value("${admin.default.password}")
    private String adminPassword;

    @PostConstruct
    public void initialize() {
        boolean adminIsExist = userRepository.existsByEmail(adminEmail);
        if (!adminIsExist) {
            byte[] randomBytes = new byte[12];
            secureRandom.nextBytes(randomBytes);
            String password = ObjectUtils.isEmpty(adminPassword)
                    ? Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
                    : adminPassword;
            log.warn("Создан пользователь {} с паролем: {}", adminEmail, password);
            userRepository.save(
                    User.builder()
                            .email(adminEmail)
                            .password(password)
                            .emailVerified(true)
                            .firstnameRu("admin")
                            .role(Role.ADMIN)
                            .lastnameRu("admin")
                            .build()
            );
        }

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (Exception e) {
            log.error("Не удалось создать каталог с файлами", e);
        }
    }
}
