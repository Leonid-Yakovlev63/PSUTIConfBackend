package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.PsutiAccountBindingCode;

public interface PsutiAccountBindingCodeRepository extends JpaRepository<PsutiAccountBindingCode, String> {
}