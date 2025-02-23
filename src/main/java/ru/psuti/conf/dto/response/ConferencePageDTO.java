package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.psuti.conf.entity.ConferencePage;

@Value
@Builder
@AllArgsConstructor
public class ConferencePageDTO {

    public String path;
    
    public String pageNameRu;
    
    public String pageNameEn;
    
    public String htmlContentRu;
    
    public String htmlContentEn;

    public Boolean isEnabled = false;

    public ConferencePageDTO(ConferencePage conferencePage){
        this.path = conferencePage.getPath();
        this.pageNameRu = conferencePage.getPageNameRu();
        this.pageNameEn = conferencePage.getPageNameEn();
        this.htmlContentRu = conferencePage.getHtmlContentRu();
        this.htmlContentEn = conferencePage.getHtmlContentEn();
    }
    
}
