package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.psuti.conf.dto.request.CreateConferenceDto;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.entity.Conference;
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
        return conferenceRepository.findByEndDateGreaterThanEqual(LocalDate.now()).stream().map(CompactConference::new).collect(Collectors.toList());
    }

    public Optional<Conference> getConferenceBySlug(String slug) {

        return conferenceRepository.findConferenceBySlug(slug);
    }

    public List<Short> getYears() {
        return conferenceRepository.findYears();
    }

    public Conference createConference(CreateConferenceDto createConferenceDto){
        return conferenceRepository.save(Conference.builder()
                        .slug(createConferenceDto.getSlug())
                        .isEnglishEnabled(createConferenceDto.getIsEnglishEnabled())
                        .conferenceNameRu(createConferenceDto.getConferenceNameRu())
                        .conferenceNameEn(createConferenceDto.getConferenceNameEn())
                        .statusRu(createConferenceDto.getStatusRu())
                        .statusEn(createConferenceDto.getStatusEn())
                        .startDate(createConferenceDto.getStartDate())
                        .endDate(createConferenceDto.getEndDate())
                .build());
    }
}
