package ru.psuti.conf.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.psuti.conf.repository.*;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ScheduledService {

    private final PsutiAccountBindingCodeRepository psutiAccountBindingCodeRepository;

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    private final EmailChangeCodeRepository emailChangeCodeRepository;

    private final EmailConfirmationCodeRepository emailConfirmationCodeRepository;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        var deletedUsersCount = tokenRepository.deleteByExpiresBefore(now);
        log.info("Очищены refresh токены: {}шт", deletedUsersCount);
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void deleteExpiredAccounts() {
        LocalDateTime now = LocalDateTime.now();

        var count = userRepository.deleteByCreatedAtBeforeAndEmailVerifiedFalse(now.minusHours(12));
        log.info("Очищены не подтверждённые пользователи: {}шт", count);

        var deletedEmailConfirmCount = emailConfirmationCodeRepository.deleteByExpiresBefore(now);
        log.info("Очищены сообщения подтверждения почт: {}шт", deletedEmailConfirmCount);

        var deletedEmailChangeCount = emailChangeCodeRepository.deleteByExpiresBefore(now);
        log.info("Очищены сообщения подтверждения смены почт: {}шт", deletedEmailChangeCount);

        var deletedPasswordResetCount = passwordResetCodeRepository.deleteByExpiresBefore(now);
        log.info("Очищены сообщения востановления паролей: {}шт", deletedPasswordResetCount);

        var deletedPsutiAccountBindingCount = psutiAccountBindingCodeRepository.deleteByExpiresBefore(now);
        log.info("Очищены сообщения привязки почт к аккаунту пгути: {}шт", deletedPsutiAccountBindingCount);
    }
}
