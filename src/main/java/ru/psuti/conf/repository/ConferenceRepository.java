package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.dto.request.ConferenceInfoDTO;
import ru.psuti.conf.dto.request.ConferenceSettingsDTO;
import ru.psuti.conf.entity.Conference;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @Query(value = "SELECT * FROM conferences AS c WHERE extract(year from c.start_date) = :year AND is_enabled = true", nativeQuery = true)
    List<Conference> findActiveConferencesByYear(@Param("year") Short year);

    @Query(value = "SELECT * FROM conferences AS c WHERE is_enabled = false", nativeQuery = true)
    List<Conference> findInactiveConferences();

    Optional<Conference> findConferenceBySlug(String slug);

    List<Conference> findByEndDateGreaterThanEqualAndIsEnabledTrue(LocalDate now);

    @Query(value = "SELECT DISTINCT EXTRACT(YEAR FROM c.start_date) AS year FROM conferences AS c WHERE c.is_enabled ORDER BY year ASC", nativeQuery = true)
    List<Short> findYears();

    boolean existsBySlug(String slug);

    @Modifying
    @Transactional
    @Query(value = "UPDATE conferences SET " +
            "slug = :#{#dto.slug}, " +
            "conference_name_ru = :#{#dto.conferenceNameRu}, " +
            "conference_name_en = :#{#dto.conferenceNameEn}, " +
            "status_ru = :#{#dto.statusRu}, " +
            "status_en = :#{#dto.statusEn}, " +
            "start_date = :#{#dto.startDate}, " +
            "end_date = :#{#dto.endDate}, " +
            "meeting_point_ru = :#{#dto.meetingPointRu}, " +
            "meeting_point_en = :#{#dto.meetingPointEn}, " +
            "web_site = :#{#dto.webSite}, " +
            "email = :#{#dto.email}, " +
            "phone = :#{#dto.phone}, " +
            "closing_date_for_applications = :#{#dto.closingDateForApplications}, " +
            "closing_date_for_registrations = :#{#dto.closingDateForRegistrations}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE slug = :slug",
            nativeQuery = true)
    void updateConferenceInfo(@Param("slug") String slug, @Param("dto") ConferenceInfoDTO dto);

    @Modifying
    @Transactional
    @Query(value = "UPDATE conferences SET " +
            "is_enabled = :#{#dto.isEnabled}, " +
            "is_enabled_for_registration = :#{#dto.isEnabledForRegistration}, " +
            "is_english_enabled = :#{#dto.isEnglishEnabled}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE slug = :slug",
            nativeQuery = true)
    void updateConferenceSettings(@Param("slug") String slug,
                                  @Param("dto") ConferenceSettingsDTO dto);
}
