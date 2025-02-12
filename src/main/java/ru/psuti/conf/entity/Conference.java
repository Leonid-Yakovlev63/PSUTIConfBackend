package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "is_english_enabled", nullable = false)
    private Boolean isEnglishEnabled;

    @Column(name = "conference_name_ru", nullable = false)
    private String conferenceNameRu;

    @Column(name = "conference_name_en")
    private String conferenceNameEn;

    @Column(name = "status_ru")
    private String statusRu;

    @Column(name = "status_en")
    private String statusEn;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "closing_date_for_applications")
    private ZonedDateTime closingDateForApplications;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.MERGE)
    private List<ConferenceSection> conferenceSections = new ArrayList<ConferenceSection>();

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "conferences_organizers",
            joinColumns = @JoinColumn(name = "conference_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<ConferenceOrganizer> conferenceOrganizers = new ArrayList<ConferenceOrganizer>();


    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "conferences_admins",
            joinColumns = @JoinColumn(name = "conference_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> admins = new ArrayList<User>();

    @OneToMany(mappedBy = "conference", cascade = CascadeType.MERGE)
    private List<ConferencePage> conferencePages = new ArrayList<ConferencePage>();
}
