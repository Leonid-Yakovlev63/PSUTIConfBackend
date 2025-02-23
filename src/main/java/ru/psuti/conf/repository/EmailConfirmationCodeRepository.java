package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.confirms.EmailConfirmationCode;
import ru.psuti.conf.entity.auth.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailConfirmationCodeRepository extends JpaRepository<EmailConfirmationCode, String> {

    Optional<EmailConfirmationCode> getFirstByUserOrderByCreatedAtDesc(User user);

    Optional<EmailConfirmationCode> getByCode(String code);

    long deleteByExpiresBefore(LocalDateTime expires);

}