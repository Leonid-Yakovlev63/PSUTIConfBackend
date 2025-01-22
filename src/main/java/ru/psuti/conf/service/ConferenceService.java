package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.repository.ConferenceRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    public List<Conference> getConferences() {
        return conferenceRepository.findAll();
    }

    public Optional<Conference> getConferenceById(Long id) {
        return conferenceRepository.findById(id);
    }

    public List<CompactConference> getConferencesByYear(Short year) {
        return conferenceRepository.findConferencesByYear(year).stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public List<CompactConference> getCurrentConferences() {
        return conferenceRepository.findByEndDateGreaterThanEqual(LocalDate.now()).stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public Optional<Conference> getConferenceBySlug(String slug) {

        return conferenceRepository.findConferenceBySlug(slug);
    }

    public List<Short> getYears() {
        return conferenceRepository.findYears();
    }
}
