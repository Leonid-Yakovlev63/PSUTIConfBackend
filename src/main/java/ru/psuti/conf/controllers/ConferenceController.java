package ru.psuti.conf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.psuti.conf.dto.request.CreateConferenceDto;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.Role;
import ru.psuti.conf.entity.User;
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
    public ResponseEntity<Optional<CompactConference>> getConferenceById(@PathVariable Long id) {

        Optional<Conference> optionalConference = conferenceService.getConferenceById(id);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                return new ResponseEntity<>(optionalConference.map(CompactConference::new), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                return new ResponseEntity<>(optionalConference.map(CompactConference::new), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Optional<CompactConference>> getConferenceBySlug(@PathVariable String slug) {

        Optional<Conference> optionalConference = conferenceService.getConferenceBySlug(slug);

        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getIsEnabled()) {
                return new ResponseEntity<>(optionalConference.map(CompactConference::new), HttpStatus.OK);
            }
            if (hasPermission(conference)) {
                return new ResponseEntity<>(optionalConference.map(CompactConference::new), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity<String> createConference(@RequestBody CreateConferenceDto createConferenceDto) {
        conferenceService.createConference(createConferenceDto);
        return new ResponseEntity<String>("Conference create successfully", HttpStatus.CREATED);
    }

    private boolean hasPermission(Conference conference){
        Optional<User> optionalUser = UserService.getCurrentUser();
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        if(user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        for (User admin: conference.getAdmins()){
            if (Objects.equals(admin.getId(), user.getId()))
                return true;
        }
        return false;
    }

}