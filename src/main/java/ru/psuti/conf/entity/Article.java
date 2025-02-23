package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.auth.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_ru")
    private String titleRu;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "description_en")
    private String descriptionEn;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private ConferenceSection section;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "articles_authors",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> authors = new ArrayList<User>();

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.PENDING;

    private Short version;

    @ManyToOne
    @JoinColumn(name = "application_for_participation_id", nullable = false)
    private ApplicationForParticipation applicationForParticipation;
}
