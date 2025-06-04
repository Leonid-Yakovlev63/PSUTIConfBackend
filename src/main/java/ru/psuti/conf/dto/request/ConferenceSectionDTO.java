package ru.psuti.conf.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Value;

@Value
public class ConferenceSectionDTO {

    Long id;

    String sectionNameRu;

    String sectionNameEn;

    String placeRu;

    String placeEn;

}
