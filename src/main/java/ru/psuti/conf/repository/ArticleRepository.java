package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferenceSection;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findBySection_Conference(Conference conference);

    List<Article> findBySection_ConferenceAndUserId(Conference conference, UUID userId);
}
