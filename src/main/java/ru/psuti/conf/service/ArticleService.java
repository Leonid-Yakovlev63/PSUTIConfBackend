package ru.psuti.conf.service;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.CreateArticleDTO;
import ru.psuti.conf.entity.*;
import ru.psuti.conf.entity.auth.PermissionFlags;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.repository.ArticleRepository;
import ru.psuti.conf.repository.ConferenceSectionRepository;
import ru.psuti.conf.repository.FileRepository;
import ru.psuti.conf.security.CustomAuthenticationEntryPoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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

    @Autowired
    private FileRepository fileRepository;

    @Value("${files.upload-dir}/private")
    private String FILE_UPLOAD_DIR;

    @Autowired
    private ConferenceSectionRepository conferenceSectionRepository;

    public Optional<Article> createArticle(CreateArticleDTO createArticleDTO) {
        Optional<ConferenceSection> optionalConferenceSection = conferenceSectionRepository.findById(createArticleDTO.getSectionId());
        if(optionalConferenceSection.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("conference section with id = {%d} not found", createArticleDTO.getSectionId()));
        }
        ConferenceSection conferenceSection = optionalConferenceSection.get();

        Conference conference = conferenceSection.getConference();

        User user = UserService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        ZonedDateTime endDate = conference.getClosingDateForRegistrations() != null
                ? conference.getClosingDateForRegistrations()
                : conference.getEndDate().atStartOfDay(ZoneId.of("UTC+04"));

        if (
                endDate.isAfter(ZonedDateTime.now()) &&
                !user.getRole().equals(Role.ADMIN) &&
                !user.getConferenceUserPermissions().stream()
                        .filter(cup -> Objects.equals(cup.getConference().getId(), conference.getId()))
                        .findAny()
                        .map(cup -> cup.hasAnyPermission(PermissionFlags.ADMIN, PermissionFlags.CREATE_CONF_APP))
                        .orElse(false)
        ) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Article article = Article.builder()
                .titleRu(createArticleDTO.getTitleRu())
                .titleEn(createArticleDTO.getTitleEn())
                .descriptionRu(createArticleDTO.getDescriptionRu())
                .descriptionEn(createArticleDTO.getDescriptionEn())
                .section(conferenceSection)
                .user(user)
                .version((short)1)
//                .additionalFields(createArticleDTO.getAdditionalFields())
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

    public FileInfo uploadArticleFile(Long articleId, MultipartFile file) throws IOException, NoSuchElementException {

        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if(optionalArticle.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Article with id = {%d} not found", articleId)
            );
        }

        Article article = optionalArticle.get();

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file format"
            );
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        List<String> supportedFileTypes = List.of(article.getSection().getConference().getSupportedFileFormats().toLowerCase().split(","));

        if(!supportedFileTypes.contains(fileExtension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file format: " + fileExtension
            );
        }


        FileInfo fileInfo = FileInfo.builder()
                .name(fileName)
                .size(file.getSize())
                .fileType(fileExtension)
                .article(article)
                .build();

        Path path = Paths.get(FILE_UPLOAD_DIR + fileName);

        Files.copy(file.getInputStream(), path);

        if (!Files.exists(path)) {
            throw new IOException("File was not saved successfully");
        }

        return fileRepository.save(fileInfo);

    }

}
