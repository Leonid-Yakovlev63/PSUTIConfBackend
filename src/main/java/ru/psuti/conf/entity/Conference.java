package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug; // Info

    @Builder.Default
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false; // Settings

    @Builder.Default
    @Column(name = "is_enabled_for_registration", nullable = false)
    private Boolean isEnabledForRegistration = false; // Settings

    @Column(name = "is_english_enabled", nullable = false)
    private Boolean isEnglishEnabled; // Settings

    @Column(name = "conference_name_ru", nullable = false)
    private String conferenceNameRu; // Info

    @Column(name = "conference_name_en")
    private String conferenceNameEn; // Info

    @Column(name = "status_ru")
    private String statusRu; // Info

    @Column(name = "status_en")
    private String statusEn; // Info

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "start_date")
    private LocalDate startDate; // Info

    @Column(name = "end_date")
    private LocalDate endDate; // Info

    @Column(name = "meeting_point_ru")
    private String meetingPointRu; // Info

    @Column(name = "meeting_point_en")
    private String meetingPointEn; // Info

    @Column(name = "web_site")
    private String webSite; // Info

    @Column(name = "email")
    private String email; // Info

    @Column(name = "phone")
    private String phone; // Info

    @Column(name = "closing_date_for_applications")
    private ZonedDateTime closingDateForApplications; // Info

    @Column(name = "closing_date_for_registrations")
    private ZonedDateTime closingDateForRegistrations; // Info

    @OneToMany(mappedBy = "conference", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<ConferenceSection> conferenceSections = new ArrayList<ConferenceSection>(); // Отдельная страница Sections

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "conferences_organizers",
            joinColumns = @JoinColumn(name = "conference_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<ConferenceOrganizer> conferenceOrganizers = new ArrayList<ConferenceOrganizer>();

    @OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConferenceUserPermissions> conferenceUserPermissions = new ArrayList<>(); // Отдельная страница Admins

    @OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ConferencePage> conferencePages = new ArrayList<ConferencePage>(); // Отдельная страница

    @Builder.Default
    @Column(name = "supported_file_formats", length = 2048, columnDefinition = "VARCHAR(2048) DEFAULT 'doc,docx,pdf'")
    private String supportedFileFormats = "doc,docx,pdf";
}
