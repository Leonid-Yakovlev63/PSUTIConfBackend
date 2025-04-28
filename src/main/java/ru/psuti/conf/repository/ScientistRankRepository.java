package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.ScientistRank;

public interface ScientistRankRepository extends JpaRepository<ScientistRank, Long> {
}
