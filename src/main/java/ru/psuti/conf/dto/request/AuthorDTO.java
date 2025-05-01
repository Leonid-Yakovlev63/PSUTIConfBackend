package ru.psuti.conf.dto.request;

import lombok.Value;

@Value
public class AuthorDTO {

    String email;

    String phone;

    String firstNameRu;

    String middleNameRu;

    String lastNameRu;

    String firstNameEn;

    String middleNameEn;

    String lastNameEn;

    String country;

    String city;

    String organization;

    String organizationAddress;

    Long scientificDegreeId;

    Long scientistRankId;

}
