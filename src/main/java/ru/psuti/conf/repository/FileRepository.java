package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.ArticleFileInfo;

public interface FileRepository extends JpaRepository<ArticleFileInfo, Long> {
}
