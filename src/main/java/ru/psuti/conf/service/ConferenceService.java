package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.dto.request.CreateConferenceDTO;
import ru.psuti.conf.dto.response.CompactConferenceDTO;
import ru.psuti.conf.dto.response.CompactConferencePageDTO;
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

    public List<CompactConferenceDTO> getConferences() {
        return conferenceRepository.findAll().stream().map(CompactConferenceDTO::new).collect(Collectors.toList());
    }

    public Optional<Conference> getConferenceById(Long id) {
        return conferenceRepository.findById(id);
    }

    public List<CompactConferenceDTO> getConferencesByYear(Short year) {
        return conferenceRepository.findActiveConferencesByYear(year).stream().map(CompactConferenceDTO::new).collect(Collectors.toList());
    }

    public List<CompactConferenceDTO> getCurrentConferences() {
        return conferenceRepository.findByEndDateGreaterThanEqualAndIsEnabledTrue(LocalDate.now()).stream().map(CompactConferenceDTO::new).collect(Collectors.toList());
    }

    public List<CompactConferenceDTO> getNewConferences() {
        return conferenceRepository.findInactiveConferences().stream().map(CompactConferenceDTO::new).toList();
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

    public List<CompactConferencePageDTO> getCompactConferencePagesDTO(Long id) {
        return conferencePageRepository.getCompactConferencePagesDTO(id);
    }

    @Transactional
    public Optional<Conference> createConference(CreateConferenceDTO createConferenceDto){
        if (conferenceRepository.existsBySlug(createConferenceDto.getSlug()))
            return Optional.empty();
        return Optional.of(conferenceRepository.save(
                createConferenceDto.toConference()
        ));
    }
}
