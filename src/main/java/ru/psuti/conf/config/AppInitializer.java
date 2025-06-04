package ru.psuti.conf.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.entity.auth.UserLocalized;
import ru.psuti.conf.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitializer {
    private static final SecureRandom secureRandom = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
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
            User user = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(password))
                    .emailVerified(true)
                    .preferredLocale(Locale.RU)
                    .names(Collections.singletonList(
                            UserLocalized.builder()
                                    .locale(Locale.RU)
                                    .firstName("admin")
                                    .lastName("admin")
                                    .build()
                    ))
                    .role(Role.ADMIN)
                    .build();

            user.getNames().forEach(userLocalized -> userLocalized.setUser(user));

            userRepository.save(user);
        }

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                Path privatePath = Paths.get(uploadDir + "/private");

                if (!Files.exists(privatePath)) {
                    Files.createDirectories(privatePath);
                }
                Path publicPath = Paths.get(uploadDir + "/public");

                if (!Files.exists(publicPath)) {
                    Files.createDirectories(publicPath);
                }
            }
        } catch (Exception e) {
            log.error("Не удалось создать каталог с файлами", e);
        }
    }
}
