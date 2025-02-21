package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.confirms.PsutiAccountBindingCode;

import java.time.LocalDateTime;

public interface PsutiAccountBindingCodeRepository extends JpaRepository<PsutiAccountBindingCode, String> {
    long deleteByExpiresBefore(LocalDateTime expires);
}