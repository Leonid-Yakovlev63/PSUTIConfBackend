package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.ScientificDegree;

public interface ScientificDegreeRepository extends JpaRepository<ScientificDegree, Long> {
}
