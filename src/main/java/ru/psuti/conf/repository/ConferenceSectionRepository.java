package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.ConferenceSection;

public interface ConferenceSectionRepository extends JpaRepository<ConferenceSection, Long> {
}