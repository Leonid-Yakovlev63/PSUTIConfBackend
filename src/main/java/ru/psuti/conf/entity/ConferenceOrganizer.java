package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "conference_organizer")
public class ConferenceOrganizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organizer_name_ru")
    private String organizerNameRu;

    @Column(name = "organizer_name_en")
    private String organizerNameEn;

    @Column(name = "web_site")
    private String webSite;

    private String email;

    private String phone;

}
