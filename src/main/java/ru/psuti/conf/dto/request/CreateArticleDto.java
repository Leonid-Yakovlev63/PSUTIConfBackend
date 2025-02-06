package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Value;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.entity.ConferenceSection;
import ru.psuti.conf.entity.FileInfo;

import java.util.List;

/**
 * DTO for {@link Article}
 */
@Value
public class CreateArticleDto {
    @NotEmpty
    String titleRu;
    @NotEmpty
    String titleEn;
    String descriptionRu;
    String descriptionEn;
}