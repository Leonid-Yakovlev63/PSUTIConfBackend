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
@Entity(name = "conference_pages")
public class ConferencePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "page_name_ru", nullable = false)
    private String pageNameRu;

    @Column(name = "page_name_en")
    private String pageNameEn;

    @Column(name = "html_content_ru", columnDefinition="TEXT")
    private String htmlContentRu;

    @Column(name = "html_content_en", columnDefinition="TEXT")
    private String htmlContentEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;

}
