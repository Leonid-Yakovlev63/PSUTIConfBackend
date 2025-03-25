package ru.psuti.conf.dto.request;

import lombok.Value;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Value
public class ConferenceInfoDTO {

    String slug;

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

    ZonedDateTime closingDateForApplications;

    ZonedDateTime closingDateForRegistrations;
}
