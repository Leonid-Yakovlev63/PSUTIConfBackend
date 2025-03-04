package ru.psuti.conf.dto.response;

import lombok.Value;
import ru.psuti.conf.entity.ConferencePage;

/**
 * DTO for {@link ru.psuti.conf.entity.ConferencePage}
 */
@Value
public class CompactConferencePageDTO {
    Long id;
    Integer pageIndex;
    String path;
    String pageNameRu;
    String pageNameEn;

    public CompactConferencePageDTO(ConferencePage conferencePage) {
        this.id = conferencePage.getId();
        this.pageIndex = conferencePage.getPageIndex();
        this.path = conferencePage.getPath();
        this.pageNameRu = conferencePage.getPageNameRu();
        this.pageNameEn = conferencePage.getPageNameEn();
    }

    public CompactConferencePageDTO(Long id, Integer pageIndex, String path, String pageNameRu, String pageNameEn){
        this.id = id;
        this.pageIndex = pageIndex;
        this.path = path;
        this.pageNameRu = pageNameRu;
        this.pageNameEn = pageNameEn;
    }

}