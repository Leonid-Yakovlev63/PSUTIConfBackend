package ru.psuti.conf.dto.request;

import lombok.Value;

@Value
public class ConferenceSettingsDTO {

    Boolean isEnabled;

    Boolean isEnabledForRegistration;

    Boolean isEnglishEnabled;

    String supportedFileFormats;

}
