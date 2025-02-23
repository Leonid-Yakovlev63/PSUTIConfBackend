package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.ConferenceSection;

@Value
@AllArgsConstructor
public class ConferenceSectionDto {

    Long id;
    String sectionName;
    String description;
    String place;

    public ConferenceSectionDto(ConferenceSection section) {
        this.id = section.getId();
        this.sectionName = section.getSectionName();
        this.description = section.getDescription();
        this.place = section.getPlace();
    }
}
