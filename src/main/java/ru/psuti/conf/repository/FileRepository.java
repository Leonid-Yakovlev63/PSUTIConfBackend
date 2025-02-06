package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.FileInfo;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
}
