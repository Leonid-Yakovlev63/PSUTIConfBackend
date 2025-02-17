package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "meeting_point_ru")
    private String meetingPointRu;

    @Column(name = "meeting_point_en")
    private String meetingPointEn;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

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
