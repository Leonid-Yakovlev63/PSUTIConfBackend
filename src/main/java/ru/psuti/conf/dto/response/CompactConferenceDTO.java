package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.Conference;

import java.time.LocalDate;

/**
 * DTO for {@link ru.psuti.conf.entity.Conference}
 */
@Value
@AllArgsConstructor
public class CompactConferenceDTO {
    Long id;
    String slug;
    Boolean isEnabled;
    Boolean isEnglishEnable;
    String conferenceNameRu;
    String conferenceNameEn;
    String statusRu;
    String statusEn;
    LocalDate startDate;
    LocalDate endDate;

    public CompactConferenceDTO(Conference conference) {
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
    }

}
