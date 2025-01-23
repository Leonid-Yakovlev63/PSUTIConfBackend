package ru.psuti.conf.controllers;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.request.SignIn;
import ru.psuti.conf.dto.request.SignUp;
import ru.psuti.conf.dto.response.AuthenticationSuccess;
import ru.psuti.conf.entity.Token;
import ru.psuti.conf.entity.User;
import ru.psuti.conf.repository.TokenRepository;
import ru.psuti.conf.service.JwtService;
import ru.psuti.conf.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    public AuthenticationSuccess signIn(@RequestBody @Valid SignIn request, HttpServletResponse response) throws IOException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        if (!user.isEnabled()) {
            response.sendError(403, "Please verify your email address to activate your account.");
            return null;
        }

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

        return new AuthenticationSuccess(accessToken);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUp request, HttpServletResponse response) throws IOException {
        Optional<User> user = userService.createByEmail(request.toUser(passwordEncoder));

        if (user.isEmpty()) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "User email already in use.");
            return null;
        }

        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body("Confirm email.");
    }

    @PostMapping("/refresh")
    public AuthenticationSuccess refreshToken(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
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
            // cookie.setSecure(true);
            cookie.setPath("/api/auth/refresh");
            cookie.setMaxAge(30 * 24 * 60 * 60);

            response.addCookie(cookie);

            return new AuthenticationSuccess(accessToken);
        }
        return null;
    }

    @PostMapping("/sign-out")
    public boolean signOut(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        String refreshToken = getRefreshToken(request.getCookies());

        if (!ObjectUtils.isEmpty(refreshToken)) {
            tokenRepository.deleteByToken(refreshToken);
        }

        var cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
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

}
