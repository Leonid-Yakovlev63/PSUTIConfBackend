package ru.psuti.conf.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.entity.ArticleAuthor;
import ru.psuti.conf.entity.Status;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CompactArticleDTO {
    private Long id;
    private String titleRu;
    private String titleEn;
    private String descriptionRu;
    private String descriptionEn;
    private Long sectionId;
    private List<Long> authorIds;
    private Status status;
    private Short version;
    private UUID userId;

    public static CompactArticleDTO from(Article article) {
        return CompactArticleDTO.builder()
                .id(article.getId())
                .titleRu(article.getTitleRu())
                .titleEn(article.getTitleEn())
                .descriptionRu(article.getDescriptionRu())
                .descriptionEn(article.getDescriptionEn())
                .sectionId(article.getSection() != null ? article.getSection().getId() : null)
                .authorIds(article.getAuthors().stream().map(a -> a.getId()).toList())
                .status(article.getStatus())
                .version(article.getVersion())
                .userId(article.getUser() != null ? article.getUser().getId() : null)
                .build();
    }

}
