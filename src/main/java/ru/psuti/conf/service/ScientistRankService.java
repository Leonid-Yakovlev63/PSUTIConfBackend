package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.psuti.conf.entity.ScientificDegree;
import ru.psuti.conf.entity.ScientistRank;
import ru.psuti.conf.repository.ScientistRankRepository;

import java.util.Optional;

@Service
public class ScientistRankService {

    @Autowired
    private ScientistRankRepository scientistRankRepository;

    public Optional<ScientistRank> getScientistRankById(Long id) {
        return scientistRankRepository.findById(id);
    }
}
