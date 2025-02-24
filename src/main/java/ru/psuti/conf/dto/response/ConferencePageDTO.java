package ru.psuti.conf.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    @JsonCreator
    public ConferencePageDTO(
            @JsonProperty("path") String path,
            @JsonProperty("pageNameRu") String pageNameRu,
            @JsonProperty("pageNameEn") String pageNameEn,
            @JsonProperty("htmlContentRu") String htmlContentRu,
            @JsonProperty("htmlContentEn") String htmlContentEn
    ) {
        this.path = path;
        this.pageNameRu = pageNameRu;
        this.pageNameEn = pageNameEn;
        this.htmlContentRu = htmlContentRu;
        this.htmlContentEn = htmlContentEn;
    }

    public ConferencePageDTO(ConferencePage conferencePage){
        this.path = conferencePage.getPath();
        this.pageNameRu = conferencePage.getPageNameRu();
        this.pageNameEn = conferencePage.getPageNameEn();
        this.htmlContentRu = conferencePage.getHtmlContentRu();
        this.htmlContentEn = conferencePage.getHtmlContentEn();
    }
    
}
