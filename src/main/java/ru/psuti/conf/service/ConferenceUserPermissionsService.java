package ru.psuti.conf.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.AddAdminForConferenceDTO;
import ru.psuti.conf.dto.response.ConferenceAdminDTO;
import ru.psuti.conf.dto.response.auth.CompactNamesDTO;
import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.entity.auth.UserLocalized;
import ru.psuti.conf.repository.ConferenceRepository;
import ru.psuti.conf.repository.ConferenceUserPermissionsRepository;
import ru.psuti.conf.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConferenceUserPermissionsService {

    private final ConferenceRepository conferenceRepository;

    private final UserRepository userRepository;

    private final ConferenceUserPermissionsRepository conferenceUserPermissionsRepository;

    @Transactional
    public void addAdminForConference(String slug, AddAdminForConferenceDTO addAdminForConferenceDTO) {
        Optional<Conference> optionalConference = conferenceRepository.findConferenceBySlug(slug);
        if (optionalConference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conference not found");
        }

        Conference conference = optionalConference.get();

        Optional<User> optionalUser = userRepository.findByEmail(addAdminForConferenceDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();

        conferenceUserPermissionsRepository.save(
                ConferenceUserPermissions.builder()
                        .permissions(addAdminForConferenceDTO.getPermissions())
                        .user(user)
                        .conference(conference)
                        .build()
        );
    }

    public List<ConferenceAdminDTO> getConferenceAdmins(String slug) {
        Optional<Conference> optionalConference = conferenceRepository.findConferenceBySlug(slug);
        if(optionalConference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conference not found");
        }
        Conference conference = optionalConference.get();

        List<ConferenceUserPermissions> permissions = conference.getConferenceUserPermissions();

        return permissions.stream().map(v -> ConferenceAdminDTO.builder()
                .id(v.getUser().getId())
                .names(v.getUser().getNames().stream().collect(
                        Collectors.toMap(
                                UserLocalized::getLocale,
                                CompactNamesDTO::new)
                        )
                )
                .email(v.getUser().getEmail())
                .permissions(v.getPermissions())
                .build()
        ).toList();
    }

}
