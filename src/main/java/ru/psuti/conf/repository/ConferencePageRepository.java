package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.dto.response.CompactConferencePageDTO;
import ru.psuti.conf.entity.ConferencePage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferencePageRepository extends JpaRepository<ConferencePage, Long> {
    public Optional<ConferencePage> getConferencePageByPathAndConference_Slug(String path, String slug);

    @Query("SELECT new ru.psuti.conf.dto.response.CompactConferencePageDTO(c.id, c.path, c.pageNameRu, c.pageNameEn) FROM ConferencePage c WHERE c.conference.id = :id")
    public List<CompactConferencePageDTO> getCompactConferencePagesDTO(Long id);

    @Query("SELECT cp FROM ConferencePage cp WHERE cp.conference.slug = :slug")
    List<ConferencePage> findAllByConferenceSlug(String slug);

    @Modifying
    @Transactional
    @Query("UPDATE ConferencePage cp SET cp.isEnabled = true WHERE cp.id = :id")
    int activateConferencePage(@Param("id") Long id);
}
