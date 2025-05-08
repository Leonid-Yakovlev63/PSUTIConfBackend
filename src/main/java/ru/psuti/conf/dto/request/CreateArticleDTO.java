package ru.psuti.conf.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Value
public class CreateArticleDTO {

     String titleRu;

     String titleEn;

     String descriptionRu;

     String descriptionEn;

     @NotNull
     Long conferenceId;

     Long sectionId;

     List<AuthorDTO> authors;

//     Map<String, Object> additionalFields;
}
