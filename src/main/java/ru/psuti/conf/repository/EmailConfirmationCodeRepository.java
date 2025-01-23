package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.EmailConfirmationCode;

import java.time.LocalDateTime;

public interface EmailConfirmationCodeRepository extends JpaRepository<EmailConfirmationCode, String> {
    long deleteByExpiresBefore(LocalDateTime expires);
}