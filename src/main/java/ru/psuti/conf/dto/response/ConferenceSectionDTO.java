package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.ConferenceSection;

@Value
@AllArgsConstructor
public class ConferenceSectionDTO {

    Long id;
    String sectionNameRu;
    String sectionNameEn;
    String placeRu;
    String placeEn;
    Boolean isDefault;

    public ConferenceSectionDTO(ConferenceSection section) {
        this.id = section.getId();
        this.sectionNameRu = section.getSectionNameRu();
        this.sectionNameEn = section.getSectionNameEn();
        this.placeRu = section.getPlaceRu();
        this.placeEn = section.getPlaceEn();
        this.isDefault = section.getIsDefault();
    }
}
