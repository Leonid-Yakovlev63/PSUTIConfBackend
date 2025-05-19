package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.ConferenceSection;

@Repository
public interface ConferenceSectionRepository extends JpaRepository<ConferenceSection, Long> {

}
