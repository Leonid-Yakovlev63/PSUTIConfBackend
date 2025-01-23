package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.PasswordResetCode;

import java.time.LocalDateTime;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, String> {
    long deleteByExpiresBefore(LocalDateTime expires);
}