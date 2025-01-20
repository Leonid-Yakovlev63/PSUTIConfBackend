package ru.psuti.conf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.response.CompactConference;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.service.ConferenceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @GetMapping
    public List<Conference> getConferences() {
        return conferenceService.getConferences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Conference>> getConferenceById(@PathVariable Long id) {

        Optional<Conference> conference = conferenceService.getConferenceById(id);

        if (conference.isPresent()) {
            return new ResponseEntity<>(conference, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Optional<Conference>> getConferenceBySlug(@PathVariable String slug) {

        Optional<Conference> conference = conferenceService.getConferenceBySlug(slug);

        if (conference.isPresent()) {
            return new ResponseEntity<>(conference, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/year/{year}")
    public List<CompactConference> getConferencesByYear(@PathVariable Short year) {
        return conferenceService.getConferencesByYear(year);
    }


}
