package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.ConferenceSectionDTO;
import ru.psuti.conf.dto.response.ConferencePageDTO;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferencePage;
import ru.psuti.conf.entity.ConferenceSection;
import ru.psuti.conf.repository.ConferenceRepository;
import ru.psuti.conf.repository.ConferenceSectionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ConferenceSectionService {

    @Autowired
    private ConferenceSectionRepository conferenceSectionRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    public void updateConferenceSections(String slug, List<ConferenceSectionDTO> conferenceSectionDTOs) {
        Optional<Conference> optionalConference = conferenceRepository.findConferenceBySlug(slug);
        if (optionalConference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Conference with slug = {%s} not found", slug));
        }

        Conference conference = optionalConference.get();

        List<ConferenceSection> conferenceSections = conference.getConferenceSections();

        for(ConferenceSectionDTO conferenceSectionDTO: conferenceSectionDTOs) {
            Optional<ConferenceSection> existingSectionOpt = conferenceSections.stream()
                    .filter(page -> conferenceSectionDTO.getId() != null && page.getId().equals(conferenceSectionDTO.getId()))
                    .findFirst();

            if (existingSectionOpt.isPresent()) {
                ConferenceSection existingSection = existingSectionOpt.get();
                existingSection.setSectionNameRu(conferenceSectionDTO.getSectionNameRu());
                existingSection.setSectionNameEn(conferenceSectionDTO.getSectionNameEn());
                existingSection.setPlaceRu(conferenceSectionDTO.getPlaceRu());
                existingSection.setPlaceEn(conferenceSectionDTO.getPlaceEn());
                conferenceSectionRepository.save(existingSection);
            } else {
                ConferenceSection newConferenceSection = ConferenceSection.builder()
                        .sectionNameRu(conferenceSectionDTO.getSectionNameRu())
                        .sectionNameEn(conferenceSectionDTO.getSectionNameEn())
                        .placeRu(conferenceSectionDTO.getPlaceRu())
                        .placeEn(conferenceSectionDTO.getPlaceEn())
                        .conference(conference)
                        .isDefault(false)
                        .build();
                conferenceSectionRepository.save(newConferenceSection);
            }
        }

        List<Long> idsFromRequest = conferenceSectionDTOs.stream()
                .map(ConferenceSectionDTO::getId)
                .toList();

        List<ConferenceSection> sectionsToDelete = conferenceSections.stream()
                .filter(section -> !idsFromRequest.contains(section.getId()))
                .toList();

        for (ConferenceSection section : sectionsToDelete) {
            conferenceSectionRepository.delete(section);
        }
    }
}
