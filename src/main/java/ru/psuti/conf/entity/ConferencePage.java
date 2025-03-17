package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "conference_pages")
public class ConferencePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_index")
    private Integer pageIndex;

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

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;

}
