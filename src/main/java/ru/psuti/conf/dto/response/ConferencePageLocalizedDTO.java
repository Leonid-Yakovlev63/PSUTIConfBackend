package ru.psuti.conf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatusCode;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.ConferencePage;

@Value
@Builder
@AllArgsConstructor
public class ConferencePageLocalizedDTO {

    public String lang;

    public String path;

    public String pageName;

    public String htmlContent;

}
