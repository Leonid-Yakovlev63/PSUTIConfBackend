package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.psuti.conf.entity.Conference;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * DTO for {@link ru.psuti.conf.entity.Conference}
 */
@Value
public class CreateConferenceDto {

    @NotBlank
    @Length(max = 255)
    String slug;

    Boolean isEnglishEnabled;

    @NotBlank
    @Length(max = 255)
    String conferenceNameRu;

    @NotBlank
    @Length(max = 255)
    String conferenceNameEn;

    String statusRu;

    String statusEn;

    ZonedDateTime startDate;

    ZonedDateTime endDate;

    public Conference toConference() {
        return Conference.builder()
                .slug(slug)
                .isEnglishEnabled(isEnglishEnabled)
                .conferenceNameRu(conferenceNameRu)
                .conferenceNameEn(conferenceNameEn)
                .statusRu(statusRu)
                .statusEn(statusEn)
                .startDate(startDate)
                .endDate(endDate)
                .isEnabled(false)
                .build();
    }
}