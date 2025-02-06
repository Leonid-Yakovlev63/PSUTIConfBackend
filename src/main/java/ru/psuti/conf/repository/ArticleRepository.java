package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
