package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.dto.request.CreateConferenceDto;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferencePage;
import ru.psuti.conf.repository.ConferencePageRepository;
import ru.psuti.conf.repository.ConferenceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferencePageRepository conferencePageRepository;

    public List<CompactConference> getConferences() {
        return conferenceRepository.findAll().stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public Optional<Conference> getConferenceById(Long id) {
        return conferenceRepository.findById(id);
    }

    public List<CompactConference> getConferencesByYear(Short year) {
        return conferenceRepository.findActiveConferencesByYear(year).stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public List<CompactConference> getCurrentConferences() {
        return conferenceRepository.findByEndDateGreaterThanEqualAndIsEnabledTrue(LocalDate.now()).stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public List<CompactConference> getNewConferences() {
        return conferenceRepository.findInactiveConferences().stream().map(CompactConference::new).toList();
    }

    public Optional<Conference> getConferenceBySlug(String slug) {

        return conferenceRepository.findConferenceBySlug(slug);
    }

    public List<Short> getYears() {
        return conferenceRepository.findYears();
    }

    public Optional<ConferencePage> getConferencePageBySlugAndPath(String slug, String path) {
        return conferencePageRepository.getConferencePageByPathAndConference_Slug(path, slug);
    }

    @Transactional
    public Optional<Conference> createConference(CreateConferenceDto createConferenceDto){
        if (conferenceRepository.existsBySlug(createConferenceDto.getSlug()))
            return Optional.empty();
        return Optional.of(conferenceRepository.save(
                createConferenceDto.toConference()
        ));
    }
}
