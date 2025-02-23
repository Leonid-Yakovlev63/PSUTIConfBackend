package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.psuti.conf.entity.ConferenceOrganizer;

@Value
@AllArgsConstructor
public class ConferenceOrganizerDto {

    Long id;
    String organizerNameRu;
    String organizerNameEn;
    String webSite;
    String email;
    String phone;

    public ConferenceOrganizerDto(ConferenceOrganizer organizer) {
        this.id = organizer.getId();
        this.organizerNameRu = organizer.getOrganizerNameRu();
        this.organizerNameEn = organizer.getOrganizerNameEn();
        this.webSite = organizer.getWebSite();
        this.email = organizer.getEmail();
        this.phone = organizer.getPhone();
    }
}
