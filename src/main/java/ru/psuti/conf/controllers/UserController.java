package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.psuti.conf.dto.request.UpdateUserDTO;

import ru.psuti.conf.dto.response.auth.CompactUserDTO;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.service.UserService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<CompactUserDTO> getUserById(@PathVariable UUID id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            CompactUserDTO userDto = new CompactUserDTO(userOptional.get());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable UUID id) {
        try {
            Optional<User> updatedUser = userService.updateUser(updateUserDTO, id);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserMe(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        try {
            User user = UserService.getCurrentUser().get();
            Optional<User> updatedUser = userService.updateUser(updateUserDTO, user.getId());
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
