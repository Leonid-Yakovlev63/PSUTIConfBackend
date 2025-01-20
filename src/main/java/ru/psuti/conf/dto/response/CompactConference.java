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
public class CompactConference {
    Long id;
    String slug;
    Boolean isEnglishEnable;
    String conferenceNameRu;
    String conferenceNameEn;
    String statusRu;
    String statusEn;
    LocalDate startDate;
    LocalDate endDate;

    public CompactConference(Conference conference) {
        this.id = conference.getId();
        this.slug = conference.getSlug();
        this.isEnglishEnable = conference.getIsEnglishEnable();
        this.conferenceNameRu = conference.getConferenceNameRu();
        this.conferenceNameEn = conference.getConferenceNameEn();
        this.statusRu = conference.getStatusRu();
        this.statusEn = conference.getStatusEn();
        this.startDate = conference.getStartDate();
        this.endDate = conference.getEndDate();
    }

}
