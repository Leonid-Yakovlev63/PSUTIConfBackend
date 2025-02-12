package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "files")
public class ArticleFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String localization;

    private Long size;

    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @Column(name = "file_type")
    private FileType fileType;

}
