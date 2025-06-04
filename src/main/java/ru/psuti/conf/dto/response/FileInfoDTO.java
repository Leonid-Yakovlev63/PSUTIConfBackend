package ru.psuti.conf.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class FileInfoDTO {

    Long id;

    String name;

    Long size;

    LocalDate uploadDate;

    String fileType;

    Long articleId;

}
