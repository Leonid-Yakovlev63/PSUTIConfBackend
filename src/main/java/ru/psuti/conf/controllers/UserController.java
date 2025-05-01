package ru.psuti.conf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public Optional<CompactUserDTO> getUserInfo() {
        return UserService.getCurrentUser().map(CompactUserDTO::new);
    }

    /*
    * Пример запроса
    * GET http://localhost:8080/api/users?page=0&size=10
    * */
    @GetMapping
    public Page<CompactUserDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

}
