package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.Conference;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @Query(value = "SELECT * FROM conferences AS c WHERE extract(year from c.start_date) = :year", nativeQuery = true)
    List<Conference> findConferencesByYear(@Param("year") Short year);

    Optional<Conference> findConferenceBySlug(String slug);

}
