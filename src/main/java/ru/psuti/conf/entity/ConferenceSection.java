package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "conference_sections")
public class ConferenceSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_name_ru", nullable = false)
    private String sectionNameRu;

    @Column(name = "section_name_en")
    private String sectionNameEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference")
    private Conference conference;

    @Column(name = "place_ru")
    private String placeRu;

    @Column(name = "place_en")
    private String placeEn;

    @Column(name = "is_default", columnDefinition = "boolean default true")
    private Boolean isDefault;
}
