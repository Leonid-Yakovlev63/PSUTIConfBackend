package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.AddAdminForConferenceDTO;
import ru.psuti.conf.dto.request.ConferenceInfoDTO;
import ru.psuti.conf.dto.request.ConferenceSectionDTO;
import ru.psuti.conf.dto.request.ConferenceSettingsDTO;
import ru.psuti.conf.dto.request.CreateConferenceDTO;
import ru.psuti.conf.dto.response.*;
import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.*;
import ru.psuti.conf.entity.Locale;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;
import ru.psuti.conf.entity.auth.PermissionFlags;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.service.ConferenceSectionService;
import ru.psuti.conf.service.ConferenceService;
import ru.psuti.conf.service.ConferenceUserPermissionsService;
import ru.psuti.conf.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ConferenceSectionService conferenceSectionService;

    @Autowired
    private ConferenceUserPermissionsService conferenceUserPermissionsService;

    @GetMapping
    public List<CompactConferenceDTO> getConferences() {
        return conferenceService.getConferences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<FullConferenceDTO>> getConferenceById(@PathVariable Long id) {

        Optional<Conference> optionalConference = conferenceService.getConferenceById(id);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                return new ResponseEntity<>(optionalConference.map(c->{
                    List<CompactConferencePageDTO> compactConferenceDTOs = conferenceService.getCompactConferencePagesDTO(c.getId());
                    return new FullConferenceDTO(c, compactConferenceDTOs);
                }), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                return new ResponseEntity<>(optionalConference.map(c->{
                    List<CompactConferencePageDTO> compactConferenceDTOs = conferenceService.getCompactConferencePagesDTO(c.getId());
                    return new FullConferenceDTO(c, compactConferenceDTOs);
                }), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<FullConferenceDTO> getConferenceBySlug(@PathVariable String slug) {

        Optional<Conference> optionalConference = conferenceService.getConferenceBySlug(slug);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                List<CompactConferencePageDTO> compactConferenceDTOs = conferenceService.getCompactConferencePagesDTO(conference.getId());
                return new ResponseEntity<>(new FullConferenceDTO(conference, compactConferenceDTOs), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                List<CompactConferencePageDTO> compactConferenceDTOs = conferenceService.getCompactConferencePagesDTO(conference.getId());
                return new ResponseEntity<>(new FullConferenceDTO(conference, compactConferenceDTOs), HttpStatus.OK);
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

    @PutMapping("/slug/{slug}/subPage")
    public ResponseEntity<ConferencePage> createConferencePage(
            @RequestBody ConferencePageDTO conferencePageDTO,
            @PathVariable String slug
    ){

        if (!hasPagePermission(slug)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        try {
            ConferencePage conferencePage = conferenceService.saveConferencePage(conferencePageDTO, slug);
            return new ResponseEntity<>(conferencePage, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/slug/{slug}/subPage/{path}")
    public ResponseEntity<ConferencePageDTO> updateConferencePage(
            @PathVariable String slug,
            @PathVariable String path,
            @RequestBody ConferencePageDTO conferencePageDTO
    ) {
        try {
            ConferencePage conferencePage = conferenceService.updateConferencePage(conferencePageDTO, slug, path);
            ConferencePageDTO returningConferencePageDTO = ConferencePageDTO.builder()
                    .htmlContentRu(conferencePage.getHtmlContentRu())
                    .htmlContentEn(conferencePage.getHtmlContentEn())
                    .build();
            return new ResponseEntity<>(returningConferencePageDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    * Метод обновляющий список страниц конференции,
    * принимает список страниц, удаляет из БД те страницы, которых нет в полученном списке,
    * обновляет страницы, в которых есть изменения
    * */
    @PatchMapping("/slug/{slug}/subPages")
    public ResponseEntity<String> updateConferencePages(
            @PathVariable String slug,
            @RequestBody List<ConferencePageDTO> conferencePageDTOs
    ) {
        Set<Integer> indexes = new HashSet<>();
        for (ConferencePageDTO dto : conferencePageDTOs) {
            if (indexes.contains(dto.getPageIndex())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conference page index must be unique");
            }
            indexes.add(dto.getPageIndex());
        }
        try {
            conferenceService.updateConferencePages(slug, conferencePageDTOs);
            return ResponseEntity.ok("Conference pages updated successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conference not found");
        }
    }

    @PatchMapping("/slug/{slug}/sections")
    public ResponseEntity<String> updateConferenceSections(
            @PathVariable String slug,
            @RequestBody List<ConferenceSectionDTO> conferenceSectionDTOs
    ) {
        conferenceSectionService.updateConferenceSections(slug, conferenceSectionDTOs);
        return ResponseEntity.ok("Conference sections updated successfully");
    }

    @PutMapping("/slug/{slug}/info")
    public String updateConferenceInfo(
            @PathVariable String slug,
            @RequestBody ConferenceInfoDTO conferenceInfoDTO
    ) {
        if(conferenceService.existsBySlug(slug)){
            conferenceService.updateConferenceInfo(slug, conferenceInfoDTO);
            return "Conference updated successfully";
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Conference not found with slug: " + slug
        );
    }

    @PutMapping("/slug/{slug}/settings")
    public String updateConferenceSettings(
            @PathVariable String slug,
            @RequestBody ConferenceSettingsDTO conferenceSettingsDTO
    ) {
        if(conferenceService.existsBySlug(slug)){
            conferenceService.updateConferenceSettings(slug, conferenceSettingsDTO);
            return "Conference updated successfully";
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Conference not found with slug: " + slug
        );
    }


    @PatchMapping("/slug/{slug}/subPage/{pageId}/activate")
    public ResponseEntity<String> activateConferencePage(@PathVariable Long pageId) {
        conferenceService.activateConferencePage(pageId);
        return ResponseEntity.ok("Conference page activated successfully");
    }

    @GetMapping("/years")
    public List<Short> getYears() {
        return conferenceService.getYears();
    }

    @GetMapping("/years/current")
    public List<CompactConferenceDTO> getCurrentConferences() {
        return conferenceService.getCurrentConferences();
    }

    @GetMapping("/years/{year}")
    public List<CompactConferenceDTO> getConferencesByYear(@PathVariable Short year) {
        return conferenceService.getConferencesByYear(year);
    }

    @PostMapping()
    public ResponseEntity<String> createConference(@RequestBody @Valid CreateConferenceDTO createConferenceDto) {
        if (conferenceService.createConference(createConferenceDto).isEmpty())
            return new ResponseEntity<>("A conference with this slug already exists", HttpStatus.CONFLICT);
        return new ResponseEntity<>("Conference create successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{slug}/admins")
    public String addAdminForConference(
            @PathVariable String slug,
            @RequestBody AddAdminForConferenceDTO addAdminForConferenceDTO
    ) {
        Optional<User> optionalUser = UserService.getCurrentUser();
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        User user = optionalUser.get();

        if (!user.getRole().equals(Role.ADMIN) && !user.getConferenceUserPermissions().stream().filter(p -> slug.equals(p.getConference().getSlug())).findAny().map(p -> p.hasAnyPermission(PermissionFlags.ADMIN)).orElse(false)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        conferenceUserPermissionsService.addAdminForConference(slug, addAdminForConferenceDTO);
        return "Ok";
    }

    @DeleteMapping("/{slug}/admins/{id}")
    public ResponseEntity<String> deleteConferenceAdmin(
            @PathVariable String slug,
            @PathVariable UUID id
    ) {
        Optional<User> optionalUser = UserService.getCurrentUser();
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        User user = optionalUser.get();

        if (!user.getRole().equals(Role.ADMIN) && !user.getConferenceUserPermissions().stream().filter(p -> slug.equals(p.getConference().getSlug())).findAny().map(p -> p.hasAnyPermission(PermissionFlags.ADMIN)).orElse(false)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        conferenceUserPermissionsService.deleteConferenceAdmin(slug, id);
        return ResponseEntity.ok("The conference administrator has been successfully removed.");
    }

    @GetMapping("/{slug}/admins")
    public List<ConferenceAdminDTO> getConferenceAdmins(
            @PathVariable String slug
    ) {
        Optional<User> optionalUser = UserService.getCurrentUser();
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        User user = optionalUser.get();

        if (!user.getRole().equals(Role.ADMIN) && !user.getConferenceUserPermissions().stream().filter(p -> slug.equals(p.getConference().getSlug())).findAny().map(p -> p.hasAnyPermission(PermissionFlags.ADMIN)).orElse(false)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return conferenceUserPermissionsService.getConferenceAdmins(slug);
    }

    @GetMapping("/new")
    public List<CompactConferenceDTO> getNewConferences() {
        return conferenceService.getNewConferences();
    }

    @GetMapping("/{slug}/applications")
    public ResponseEntity<?> getApplicationsByConferenceSlug(@PathVariable String slug) {

        Optional<Conference> optionalConference = conferenceService.getConferenceBySlug(slug);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (hasPermission(conference)) {
                List<CompactArticleDTO> compactArticleDTOs = conferenceService.getApplicationsByConference(conference);
                return new ResponseEntity<>(compactArticleDTOs, HttpStatus.OK);
            }
            return new ResponseEntity<>("Access denied",HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Conference with slug %s not found".formatted(slug),HttpStatus.NOT_FOUND);
        }

    }

    private boolean hasPagePermission(String slug) {
        Optional<User> optionalUser = UserService.getCurrentUser();
        if(optionalUser.isPresent()){
            if(optionalUser.get().getRole().equals(Role.ADMIN)){
                return true;
            }
            Optional<ConferenceUserPermissions> permissions = optionalUser.get().getConferenceUserPermissions().stream().filter(p->p.getConference().getSlug().equals(slug)).findAny();
            return permissions.map(p->p.hasAnyPermission(PermissionFlags.ADMIN, PermissionFlags.WRITE_PAGES)).orElse(false);
        }
        return false;
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
