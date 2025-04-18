package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.dto.request.ConferenceInfoDTO;
import ru.psuti.conf.dto.request.ConferenceSettingsDTO;
import ru.psuti.conf.dto.request.CreateConferenceDTO;
import ru.psuti.conf.dto.response.CompactConferenceDTO;
import ru.psuti.conf.dto.response.CompactConferencePageDTO;
import ru.psuti.conf.dto.response.ConferencePageDTO;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferencePage;
import ru.psuti.conf.repository.ConferencePageRepository;
import ru.psuti.conf.repository.ConferenceRepository;

import java.time.LocalDate;
import java.util.*;
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

    public List<ConferencePage> getConferencePagesByConferenceSlug(String slug) {
        return conferencePageRepository.findAllByConferenceSlug(slug);
    }

    public boolean existsBySlug(String slug){
        return conferenceRepository.existsBySlug(slug);
    }

    @Transactional
    public ConferencePage saveConferencePage(ConferencePageDTO conferencePageDTO, String slug) {

        Optional<Conference> optionalConference = this.getConferenceBySlug(slug);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();

            Optional<ConferencePage> existingPage = conferencePageRepository
                    .getConferencePageByPathAndConference_Slug(conferencePageDTO.getPath(), slug);

            if (existingPage.isPresent()) {
                throw new IllegalArgumentException("Page with this path already exists for the conference.");
            }

            ConferencePage conferencePage = ConferencePage.builder()
                    .path(conferencePageDTO.getPath())
                    .pageNameRu(conferencePageDTO.getPageNameRu())
                    .pageNameEn(conferencePageDTO.getPageNameEn())
                    .htmlContentRu(conferencePageDTO.getHtmlContentRu())
                    .htmlContentEn(conferencePageDTO.getHtmlContentEn())
                    .isEnabled(false)
                    .conference(conference)
                    .build();

            return conferencePageRepository.save(conferencePage);
        }
        throw new NoSuchElementException("Conference not found with slug: " + slug);
    }

    public void updateConferenceInfo(String slug, ConferenceInfoDTO conferenceInfoDTO) {
        conferenceRepository.updateConferenceInfo(slug, conferenceInfoDTO);
    }

    public void updateConferenceSettings(String slug, ConferenceSettingsDTO conferenceSettingsDTO) {
        conferenceRepository.updateConferenceSettings(slug, conferenceSettingsDTO);
    }

    @Transactional
    public void activateConferencePage(Long pageId) {
        int updatedRows = conferencePageRepository.activateConferencePage(pageId);
        if (updatedRows == 0) {
            throw new NoSuchElementException("Conference page not found with id: " + pageId);
        }
    }

    @Transactional
    public ConferencePage updateConferencePage(ConferencePageDTO conferencePageDTO, String slug, String path) {
        Optional<ConferencePage> optionalConferencePage = conferencePageRepository.getConferencePageByPathAndConference_Slug(path, slug);
        if (optionalConferencePage.isPresent()){

            ConferencePage conferencePage = optionalConferencePage.get();
            conferencePage.setIsEnabled(conferencePageDTO.getIsEnabled());
            conferencePage.setHtmlContentRu(conferencePageDTO.getHtmlContentRu());
            conferencePage.setHtmlContentEn(conferencePageDTO.getHtmlContentEn());

            return conferencePageRepository.save(conferencePage);
        }
        throw new NoSuchElementException("Conference page not found");
    }

    @Transactional
    public Optional<Conference> createConference(CreateConferenceDTO createConferenceDto){
        if (conferenceRepository.existsBySlug(createConferenceDto.getSlug()))
            return Optional.empty();

        Conference conference = conferenceRepository.save(
                Conference.builder()
                        .slug(createConferenceDto.getSlug())
                        .isEnglishEnabled(createConferenceDto.getIsEnglishEnabled())
                        .conferenceNameRu(createConferenceDto.getConferenceNameRu())
                        .conferenceNameEn(createConferenceDto.getConferenceNameEn())
                        .statusRu(createConferenceDto.getStatusRu())
                        .statusEn(createConferenceDto.getStatusEn())
                        .startDate(createConferenceDto.getStartDate())
                        .endDate(createConferenceDto.getEndDate())
                        .build()
        );

        List<ConferencePage> pages = createDefaultPages(conference);

        for (ConferencePage page : pages) {
            conferencePageRepository.save(page);
        }

        return Optional.of(conference);

    }

    private List<ConferencePage> createDefaultPages(Conference conference) {
        List<ConferencePage> pages = new ArrayList<>();

        pages.add(ConferencePage.builder()
                .path("index")
                .pageNameRu("Главная")
                .pageNameEn("Home")
                .isEnabled(false)
                .conference(conference)
                .build());

        pages.add(ConferencePage.builder()
                .path("info")
                .pageNameRu("Информация")
                .pageNameEn("Information")
                .isEnabled(false)
                .conference(conference)
                .build());

        pages.add(ConferencePage.builder()
                .path("contacts")
                .pageNameRu("Контакты")
                .pageNameEn("Contacts")
                .isEnabled(false)
                .conference(conference)
                .build());

        return pages;
    }

    // +
    @Transactional
    public void updateConferencePages(String slug, List<ConferencePageDTO> conferencePageDTOs) {
        Optional<Conference> optionalConference = conferenceRepository.findConferenceBySlug(slug);
        if (optionalConference.isEmpty()) {
            throw new NoSuchElementException("Conference not found");
        }

        Conference conference = optionalConference.get();
        List<ConferencePage> conferencePages = conference.getConferencePages();

        for (ConferencePageDTO pageDTO : conferencePageDTOs) {
            Optional<ConferencePage> existingPageOpt = conferencePages.stream()
                    .filter(page -> pageDTO.getId() != null && page.getId().equals(pageDTO.getId()))
                    .findFirst();

            if (existingPageOpt.isPresent()) {

                ConferencePage existingPage = existingPageOpt.get();

                existingPage.setPath(pageDTO.getPath());
                existingPage.setPageNameRu(pageDTO.getPageNameRu());
                existingPage.setPageNameEn(pageDTO.getPageNameEn());
                existingPage.setPageIndex(pageDTO.getPageIndex());

                conferencePageRepository.save(existingPage);
            } else {
                ConferencePage newPage = ConferencePage.builder()
                        .path(pageDTO.getPath())
                        .pageNameRu(pageDTO.getPageNameRu())
                        .pageNameEn(pageDTO.getPageNameEn())
                        .pageIndex(pageDTO.getPageIndex())
                        .isEnabled(false)
                        .conference(conference)
                        .build();

                conferencePageRepository.save(newPage);
            }
        }

        List<Long> idsFromRequest = conferencePageDTOs.stream()
                .map(ConferencePageDTO::getId)
                .toList();

        List<ConferencePage> pagesToDelete = conferencePages.stream()
                .filter(page -> !idsFromRequest.contains(page.getId()))
                .toList();

        for (ConferencePage page : pagesToDelete) {
            conferencePageRepository.delete(page);
        }
    }
}
