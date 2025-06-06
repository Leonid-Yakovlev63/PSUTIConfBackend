package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
