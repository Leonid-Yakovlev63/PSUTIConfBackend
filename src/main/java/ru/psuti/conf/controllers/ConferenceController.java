package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.psuti.conf.dto.request.CreateConferenceDto;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.dto.response.ConferencePageDTO;
import ru.psuti.conf.dto.response.ConferencePageLocalizedDTO;
import ru.psuti.conf.dto.response.FullConferenceDto;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.*;
import ru.psuti.conf.entity.auth.PermissionFlags;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.service.ConferenceService;
import ru.psuti.conf.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @GetMapping
    public List<CompactConference> getConferences() {
        return conferenceService.getConferences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<FullConferenceDto>> getConferenceById(@PathVariable Long id) {

        Optional<Conference> optionalConference = conferenceService.getConferenceById(id);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                return new ResponseEntity<>(optionalConference.map(FullConferenceDto::new), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                return new ResponseEntity<>(optionalConference.map(FullConferenceDto::new), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<FullConferenceDto> getConferenceBySlug(@PathVariable String slug) {

        Optional<Conference> optionalConference = conferenceService.getConferenceBySlug(slug);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                return new ResponseEntity<>(new FullConferenceDto(conference), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                return new ResponseEntity<>(new FullConferenceDto(conference), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slug/{slug}/{subPage}/{lang}")
    public ResponseEntity<ConferencePageLocalizedDTO> getConferencePageByLang(
            @PathVariable String slug,
            @PathVariable String subPage,
            @PathVariable Locale lang
    ) {

        Optional<ConferencePage> optionalConferencePage = conferenceService.getConferencePageBySlugAndPath(slug, subPage);
        if (optionalConferencePage.isPresent()) {
            ConferencePage conferencePage = optionalConferencePage.get();
            if (lang.equals(Locale.RU)) {
                return new ResponseEntity<>(
                        ConferencePageLocalizedDTO.builder()
                                .lang(lang.name())
                                .path(conferencePage.getPath())
                                .pageName(conferencePage.getPageNameRu())
                                .htmlContent(conferencePage.getHtmlContentRu())
                                .build(),
                        HttpStatus.OK
                );
            }
            if (lang.equals(Locale.EN)) {
                return new ResponseEntity<>(
                        ConferencePageLocalizedDTO.builder()
                                .lang(lang.name())
                                .path(conferencePage.getPath())
                                .pageName(conferencePage.getPageNameEn())
                                .htmlContent(conferencePage.getHtmlContentEn())
                                .build(),
                        HttpStatus.OK
                );
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/slug/{slug}/{subPage}")
    public ResponseEntity<ConferencePageDTO> getConferenceInfo(
            @PathVariable String slug,
            @PathVariable String subPage
    ){
        Optional<ConferencePage> optionalConferencePage = conferenceService.getConferencePageBySlugAndPath(slug, subPage);
        if (optionalConferencePage.isPresent()) {
            ConferencePageDTO conferencePageDTO = new ConferencePageDTO(optionalConferencePage.get());
            return new ResponseEntity<>(conferencePageDTO, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/years")
    public List<Short> getYears() {
        return conferenceService.getYears();
    }

    @GetMapping("/years/current")
    public List<CompactConference> getCurrentConferences() {
        return conferenceService.getCurrentConferences();
    }

    @GetMapping("/years/{year}")
    public List<CompactConference> getConferencesByYear(@PathVariable Short year) {
        return conferenceService.getConferencesByYear(year);
    }

    @PostMapping()
    public ResponseEntity<String> createConference(@RequestBody @Valid CreateConferenceDto createConferenceDto) {
        if (conferenceService.createConference(createConferenceDto).isEmpty())
            return new ResponseEntity<>("A conference with this slug already exists", HttpStatus.CONFLICT);
        return new ResponseEntity<>("Conference create successfully", HttpStatus.CREATED);
    }

    @GetMapping("/new")
    public List<CompactConference> getNewConferences() {
        return conferenceService.getNewConferences();
    }

    private boolean hasPermission(Conference conference){
        return UserService.getCurrentUser()
                .filter(user -> Role.ADMIN.equals(user.getRole()) ||
                        conference.getConferenceUserPermissions().stream()
                                .anyMatch(p ->
                                        Objects.equals(p.getUser().getId(), user.getId()) &&
                                        p.hasAnyPermission(PermissionFlags.ADMIN, PermissionFlags.READ)
                                )
                ).isPresent();
    }

}
