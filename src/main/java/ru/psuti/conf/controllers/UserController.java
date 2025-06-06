package ru.psuti.conf.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.dto.response.users.FullUserDTO;
import ru.psuti.conf.dto.response.users.UserConference;
import ru.psuti.conf.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public Optional<CompactUserDTO> getUserInfo() {
        return UserService.getCurrentUser().map(CompactUserDTO::new);
    }

    @GetMapping("/me/profile")
    public Optional<FullUserDTO> getUserProfile() {
        return UserService.getCurrentUser().map(u -> new FullUserDTO(u, true));
    }

    @GetMapping("/me/conferences")
    public List<UserConference> getUserConferences() {
        return UserService.getCurrentUser().map(
                u -> u.getConferenceUserPermissions().stream()
                        .map(UserConference::new
                        ).toList()
        ).orElse(List.of());
    }

    @GetMapping
    public List<CompactUserDTO> getUsers() {
        return null;
    }

}
