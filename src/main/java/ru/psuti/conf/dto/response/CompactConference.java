package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.Conference;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link ru.psuti.conf.entity.Conference}
 */
@Value
@AllArgsConstructor
public class CompactConference {
    Long id;
    String slug;
    Boolean isEnabled;
    Boolean isEnglishEnable;
    String conferenceNameRu;
    String conferenceNameEn;
    String statusRu;
    String statusEn;
    ZonedDateTime startDate;
    ZonedDateTime endDate;
    List<CompactConferencePage> compactConferencePages;

    public CompactConference(Conference conference) {
        this.id = conference.getId();
        this.slug = conference.getSlug();
        this.isEnabled = conference.getIsEnabled();
        this.isEnglishEnable = conference.getIsEnglishEnabled();
        this.conferenceNameRu = conference.getConferenceNameRu();
        this.conferenceNameEn = conference.getConferenceNameEn();
        this.statusRu = conference.getStatusRu();
        this.statusEn = conference.getStatusEn();
        this.startDate = conference.getStartDate();
        this.endDate = conference.getEndDate();
        this.compactConferencePages = conference.getConferencePages().stream()
                .map(CompactConferencePage::new)
                .collect(Collectors.toList());
    }

}
