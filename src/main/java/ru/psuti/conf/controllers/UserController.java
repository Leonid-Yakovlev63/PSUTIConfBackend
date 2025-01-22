package ru.psuti.conf.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.response.CompactUserDto;
import ru.psuti.conf.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public Optional<CompactUserDto> getUserInfo() {
        return UserService.getCurrentUser().map(CompactUserDto::new);
    }

}
