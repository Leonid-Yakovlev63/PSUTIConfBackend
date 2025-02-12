package ru.psuti.conf.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Value;
import ru.psuti.conf.entity.Article;

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
    String udk;
    String udkLink;
}