package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.psuti.conf.entity.ScientificDegree;
import ru.psuti.conf.repository.ScientificDegreeRepository;

import java.util.Optional;

@Service
public class ScientificDegreeService {

    @Autowired
    private ScientificDegreeRepository scientificDegreeRepository;

    public Optional<ScientificDegree> getScientificDegreeById(Long id) {
        return scientificDegreeRepository.findById(id);
    }
}
