package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.util.JSONConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "articles")
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
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<ArticleAuthor> authors = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.PENDING;

    private Short version;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> additionalFields = new HashMap<>();
}
