package ru.psuti.conf.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.psuti.conf.entity.confirms.EmailConfirmationCode;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.repository.EmailConfirmationCodeRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationService {
    private static final SecureRandom secureRandom = new SecureRandom();

    @Value("${frontend.domain.global}")
    private String siteDomain;

    private final EmailConfirmationCodeRepository emailConfirmationCodeRepository;

    private final EmailService emailService;

    private final UserService userService;

    private String generateCode() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    public Long createEmailConfirmationCodeIfNotCreated(User user) {
        LocalDateTime now = LocalDateTime.now();

        var emailConfirmationCode = emailConfirmationCodeRepository.getFirstByUserOrderByCreatedAtDesc(user);

        Optional<Long> createdAt = emailConfirmationCode.map(EmailConfirmationCode::getCreatedAt)
                .map(dt -> dt.isBefore(now.minusMinutes(5)) ? null
                        : dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                );

        if (createdAt.isPresent()) {
            return createdAt.get();
        }

        createEmailConfirmationCode(user);
        return null;
    }

    public void createEmailConfirmationCode(User user) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expires = now.plusMinutes(30);
        String code = generateCode();
        long exp = expires.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        emailConfirmationCodeRepository.save(
                EmailConfirmationCode.builder()
                        .code(code+"-"+exp)
                        .createdAt(now)
                        .expires(expires)
                        .user(user)
                        .build()
        );

        String text = "Для подтверждения перейдите по ссылке: "
                + siteDomain + "/auth/confirm-email/code" + "?type=new&code=" + code + "&exp=" + exp;

        emailService.sendEmail(user.getEmail(), "Подтверждение адреса электронной почты", text);
    }

    public ResponseEntity<String> confirmEmail(String code) {
        Optional<EmailConfirmationCode> confirmationCode = emailConfirmationCodeRepository.getByCode(code);

        if (confirmationCode.isEmpty())
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Token not found");

        if (confirmationCode.get().getExpires().isBefore(LocalDateTime.now()))
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("Token expired");

        if (confirmationCode.get().getUser().isEnabled())
            return ResponseEntity.ok("Ok");

        User user = userService.getByUsername(confirmationCode.get().getUser().getUsername());
        user.setEmailVerified(true);
        userService.save(user);

        return ResponseEntity.ok("Ok");
    }

}
