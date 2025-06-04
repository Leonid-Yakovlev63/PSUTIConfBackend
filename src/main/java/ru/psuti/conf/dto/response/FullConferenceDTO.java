package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.Conference;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class FullConferenceDTO {

    Long id;
    String slug;
    Boolean isEnabled;
    Boolean isEnabledForRegistration;
    Boolean isEnglishEnabled;
    String conferenceNameRu;
    String conferenceNameEn;
    String statusRu;
    String statusEn;
    LocalDate startDate;
    LocalDate endDate;
    String meetingPointRu;
    String meetingPointEn;
    String webSite;
    String email;
    String phone;
    String supportedFileFormats;
    ZonedDateTime closingDateForApplications;
    ZonedDateTime closingDateForRegistrations;
    List<ConferenceSectionDTO> conferenceSections;
    List<ConferenceOrganizerDTO> conferenceOrganizers;
    List<CompactConferencePageDTO> pages;

    public FullConferenceDTO(Conference conference, List<CompactConferencePageDTO> conferencePageDTOs) {
        this.id = conference.getId();
        this.slug = conference.getSlug();
        this.isEnabled = conference.getIsEnabled();
        this.isEnabledForRegistration = conference.getIsEnabledForRegistration();
        this.isEnglishEnabled = conference.getIsEnglishEnabled();
        this.conferenceNameRu = conference.getConferenceNameRu();
        this.conferenceNameEn = conference.getConferenceNameEn();
        this.statusRu = conference.getStatusRu();
        this.statusEn = conference.getStatusEn();
        this.startDate = conference.getStartDate();
        this.endDate = conference.getEndDate();
        this.meetingPointRu = conference.getMeetingPointRu();
        this.meetingPointEn = conference.getMeetingPointEn();
        this.webSite = conference.getWebSite();
        this.email = conference.getEmail();
        this.phone = conference.getPhone();
        this.supportedFileFormats = conference.getSupportedFileFormats();
        this.closingDateForApplications = conference.getClosingDateForApplications();
        this.closingDateForRegistrations = conference.getClosingDateForRegistrations();

        this.conferenceSections = conference.getConferenceSections().stream()
                .map(ConferenceSectionDTO::new)
                .collect(Collectors.toList());

        this.conferenceOrganizers = conference.getConferenceOrganizers().stream()
                .map(ConferenceOrganizerDTO::new)
                .collect(Collectors.toList());

        this.pages = conferencePageDTOs;

    }
}
