package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferencePage;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class FullConferenceDto {

    Long id;
    String slug;
    Boolean isEnabled;
    Boolean isEnglishEnabled;
    String conferenceNameRu;
    String conferenceNameEn;
    String statusRu;
    String statusEn;
    String descriptionRu;
    String descriptionEn;
    LocalDate startDate;
    LocalDate endDate;
    String meetingPointRu;
    String meetingPointEn;
    String webSite;
    String email;
    String phone;
    ZonedDateTime closingDateForApplications;
    List<ConferenceSectionDto> conferenceSections;
    List<ConferenceOrganizerDto> conferenceOrganizers;


    public FullConferenceDto(Conference conference) {
        this.id = conference.getId();
        this.slug = conference.getSlug();
        this.isEnabled = conference.getIsEnabled();
        this.isEnglishEnabled = conference.getIsEnglishEnabled();
        this.conferenceNameRu = conference.getConferenceNameRu();
        this.conferenceNameEn = conference.getConferenceNameEn();
        this.statusRu = conference.getStatusRu();
        this.statusEn = conference.getStatusEn();
        this.descriptionRu = conference.getDescriptionRu();
        this.descriptionEn = conference.getDescriptionEn();
        this.startDate = conference.getStartDate();
        this.endDate = conference.getEndDate();
        this.meetingPointRu = conference.getMeetingPointRu();
        this.meetingPointEn = conference.getMeetingPointEn();
        this.webSite = conference.getWebSite();
        this.email = conference.getEmail();
        this.phone = conference.getPhone();
        this.closingDateForApplications = conference.getClosingDateForApplications();

        this.conferenceSections = conference.getConferenceSections().stream()
                .map(ConferenceSectionDto::new)
                .collect(Collectors.toList());

        this.conferenceOrganizers = conference.getConferenceOrganizers().stream()
                .map(ConferenceOrganizerDto::new)
                .collect(Collectors.toList());

    }
}
