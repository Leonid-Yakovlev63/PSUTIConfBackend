package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

}
