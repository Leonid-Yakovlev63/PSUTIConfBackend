package ru.psuti.conf.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.response.CompactArticleDTO;
import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.dto.response.users.FullUserDTO;
import ru.psuti.conf.dto.response.users.UserConference;
import ru.psuti.conf.dto.response.users.UserDTO;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/me/applications")
    public ResponseEntity<?> getMyApplications() {
        Optional<User> optionalUser = UserService.getCurrentUser();

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("User not authenticated",HttpStatus.UNAUTHORIZED);
        }

        UUID userId = optionalUser.get().getId();
        List<CompactArticleDTO> articles = userService.getUserArticles(userId);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }


    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsers().stream().map(UserDTO::new).toList();
    }

    @GetMapping("/{id}")
    public Optional<FullUserDTO> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id).map(u -> new FullUserDTO(u, true));
    }

}
