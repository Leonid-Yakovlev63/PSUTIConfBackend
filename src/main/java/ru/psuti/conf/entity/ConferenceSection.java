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

    @Column(name = "section_name")
    private String sectionName;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference")
    private Conference conference;

    private String place;

}
