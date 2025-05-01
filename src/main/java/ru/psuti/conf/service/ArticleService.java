package ru.psuti.conf.service;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.CreateArticleDTO;
import ru.psuti.conf.entity.*;
import ru.psuti.conf.repository.ArticleRepository;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ScientificDegreeService scientificDegreeService;

    @Autowired
    private ScientistRankService scientistRankService;

    public Optional<Article> createArticle(CreateArticleDTO createArticleDTO) {

        Optional<Conference> optionalConference = this.conferenceService.getConferenceById(createArticleDTO.getConferenceId());
        if(optionalConference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("conference with id = {%d} not found", createArticleDTO.getConferenceId()));
        }

        Conference conference = optionalConference.get();
        Optional<ConferenceSection> optionalConferenceSection = conference.getConferenceSections().stream().filter(conferenceSection -> Objects.equals(conferenceSection.getId(), createArticleDTO.getConferenceId())).findAny();
        if(optionalConferenceSection.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("conference section with id = {%d} not found", createArticleDTO.getSectionId()));
        }
        ConferenceSection conferenceSection = optionalConferenceSection.get();

        Article article = Article.builder()
                .titleRu(createArticleDTO.getTitleRu())
                .titleEn(createArticleDTO.getTitleEn())
                .descriptionRu(createArticleDTO.getDescriptionRu())
                .descriptionEn(createArticleDTO.getDescriptionEn())
                .section(conferenceSection)
                .user(UserService.getCurrentUser().get())
                .authors(createArticleDTO.getAuthors().stream().map(authorDTO ->
                                ArticleAuthor.builder()
                                        .email(authorDTO.getEmail())
                                        .phone(authorDTO.getPhone())
                                        .firstNameRu(authorDTO.getFirstNameRu())
                                        .lastNameRu(authorDTO.getLastNameRu())
                                        .middleNameRu(authorDTO.getMiddleNameRu())
                                        .firstNameEn(authorDTO.getFirstNameEn())
                                        .lastNameEn(authorDTO.getLastNameEn())
                                        .middleNameEn(authorDTO.getMiddleNameEn())
                                        .country(authorDTO.getCountry())
                                        .city(authorDTO.getCity())
                                        .organization(authorDTO.getOrganization())
                                        .organizationAddress(authorDTO.getOrganizationAddress())
                                        .scientificDegree(this.scientificDegreeService.getScientificDegreeById(authorDTO.getScientificDegreeId()).orElse(null))
                                        .scientistRank(this.scientistRankService.getScientistRankById(authorDTO.getScientistRankId()).orElse(null))
                                        .build()
                        ).collect(Collectors.toList()))
                .build();
        return Optional.of(this.articleRepository.save(article));
    }
}
