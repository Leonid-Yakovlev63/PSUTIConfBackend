package ru.psuti.conf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.AddAdminForConferenceDTO;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.repository.ConferenceRepository;
import ru.psuti.conf.repository.ConferenceUserPermissionsRepository;
import ru.psuti.conf.repository.UserRepository;

import java.util.Optional;

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

}
