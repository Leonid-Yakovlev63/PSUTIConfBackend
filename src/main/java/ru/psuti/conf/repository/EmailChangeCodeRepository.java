package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.confirms.EmailChangeCode;

import java.time.LocalDateTime;

public interface EmailChangeCodeRepository extends JpaRepository<EmailChangeCode, String> {
    boolean existsByNewEmail(String newEmail);

    long deleteByExpiresBefore(LocalDateTime expires);
}