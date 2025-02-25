package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.validation.anotation.ConferenceNameEnRequiredIfEnglishEnabled;

import java.time.LocalDate;

/**
 * DTO for {@link ru.psuti.conf.entity.Conference}
 */
@Value
@ConferenceNameEnRequiredIfEnglishEnabled
public class CreateConferenceDTO {

    @NotBlank
    @Length(max = 255)
    String slug;

    @NotNull
    Boolean isEnglishEnabled;

    @NotBlank
    @Length(max = 255)
    String conferenceNameRu;

    @Length(max = 255)
    String conferenceNameEn;

    String statusRu;

    String statusEn;

    LocalDate startDate;

    LocalDate endDate;
}