package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.ConferencePage;

import java.util.Optional;

@Repository
public interface ConferencePageRepository extends JpaRepository<ConferencePage, Long> {
    public Optional<ConferencePage> getConferencePageByPathAndConference_Slug(String path, String slug);
}
