package ru.psuti.conf.controllers;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.psuti.conf.dto.request.auth.SignIn;
import ru.psuti.conf.dto.request.auth.SignUpDTO;
import ru.psuti.conf.dto.response.auth.AuthenticationSuccessDTO;
import ru.psuti.conf.entity.Token;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.entity.auth.UserLocalized;
import ru.psuti.conf.repository.TokenRepository;
import ru.psuti.conf.service.ConfirmationService;
import ru.psuti.conf.service.JwtService;
import ru.psuti.conf.service.UserService;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final ConfirmationService confirmationService;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final Environment env;



    @PostMapping("/sign-in")
    public AuthenticationSuccessDTO signIn(@RequestBody @Valid SignIn request, HttpServletResponse response) throws IOException {
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        if (!user.isEnabled()) {
            response.sendError(403, "Please verify your email address to activate your account.");
            return null;
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        tokenRepository.save(Token.builder()
                .user((User) user)
                .token(refreshToken.getFirst())
                .expires(
                        refreshToken
                                .getSecond().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
                .build());

        Cookie cookie = new Cookie("refreshToken", refreshToken.getFirst());
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(30 * 24 * 60 * 60);

        response.addCookie(cookie);

        return new AuthenticationSuccessDTO(accessToken);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpDTO request, HttpServletResponse response) throws IOException {
        Optional<User> user = userService.createByEmail(
                User.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .preferredLocale(request.getPreferredLocale())
                        .names(request.getNames().entrySet().stream().map(
                                data -> UserLocalized.builder()
                                        .locale(data.getKey())
                                        .lastName(data.getValue().getLastName())
                                        .firstName(data.getValue().getFirstName())
                                        .middleName(data.getValue().getMiddleName())
                                        .build()
                        ).toList())
                        .build()
        );

        if (user.isEmpty()) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "User email already in use.");
            return null;
        }

        confirmationService.createEmailConfirmationCode(user.get());

        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body("Confirm email.");
    }

    @PostMapping("/refresh")
    @Transactional
    public AuthenticationSuccessDTO refreshToken(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshToken(request.getCookies());

        if (ObjectUtils.isEmpty(refreshToken)) {
            response.sendError(401);
            return null;
        }

        String username = null;

        try {
            username = jwtService.extractUserName(refreshToken);
        } catch (JwtException e) {
            log.debug("Обработано jwt исключение:", e);
        }

        if (ObjectUtils.isEmpty(username)) {
            response.sendError(401);
            return null;
        }

        Optional<Token> token = tokenRepository.findByToken(refreshToken);
        if (token.isEmpty()) {
            response.sendError(401);
            return null;
        }

        var user = token.get().getUser();
        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateAccessToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);
            tokenRepository.save(Token.builder()
                    .user(user)
                    .token(newRefreshToken.getFirst())
                    .expires(
                            newRefreshToken.getSecond().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                    )
                    .build());
            tokenRepository.delete(token.get());

            Cookie cookie = new Cookie("refreshToken", newRefreshToken.getFirst());
            cookie.setHttpOnly(true);
            if (!env.acceptsProfiles(Profiles.of("dev")))
                cookie.setSecure(true);
            cookie.setPath("/api/auth");
            cookie.setMaxAge(30 * 24 * 60 * 60);

            response.addCookie(cookie);

            return new AuthenticationSuccessDTO(accessToken);
        }
        return null;
    }

    @PostMapping("/sign-out")
    public boolean signOut(@NonNull HttpServletRequest request, @RequestParam(required = false) String lang, @NonNull HttpServletResponse response) {
        String refreshToken = getRefreshToken(request.getCookies());

        if (!ObjectUtils.isEmpty(refreshToken)) {
            tokenRepository.deleteByToken(refreshToken);
        }

        var cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        if (!env.acceptsProfiles(Profiles.of("dev")))
            cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return true;
    }

    private String getRefreshToken(Cookie[] cookies) {
        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<Object> getNewConfirmEmailCode(@RequestParam String email, @RequestParam(required = false) String lang, @NonNull HttpServletResponse response) throws IOException {
        User user = userService.getByUsername(email);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "");
            return null;
        }

        Long createdAt = confirmationService.createEmailConfirmationCodeIfNotCreated(user);

        if (createdAt != null) {
            return ResponseEntity.status(429).body(createdAt);
        }

        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String code) {
        return confirmationService.confirmEmail(code);
    }

}
