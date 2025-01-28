package ru.psuti.conf.dto.response;

import lombok.Value;
import ru.psuti.conf.entity.ConferencePage;

/**
 * DTO for {@link ru.psuti.conf.entity.ConferencePage}
 */
@Value
public class CompactConferencePage {
    Long id;
    String path;
    String pageNameRu;
    String pageNameEn;

    public CompactConferencePage(ConferencePage conferencePage) {
        this.id = conferencePage.getId();
        this.path = conferencePage.getPath();
        this.pageNameRu = conferencePage.getPageNameRu();
        this.pageNameEn = conferencePage.getPageNameEn();
    }

}