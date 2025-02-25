package ru.psuti.conf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.entity.auth.Role;
import ru.psuti.conf.entity.auth.User;
import ru.psuti.conf.repository.EmailChangeCodeRepository;
import ru.psuti.conf.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final EmailChangeCodeRepository emailChangeCodeRepository;

    public static Optional<User> getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof User)
                .map(User.class::cast);
    }

    public User save(User user) {
        if (user.getRole() == null)
            user.setRole(Role.USER);
        if (user.getEmailVerified() == null)
            user.setEmailVerified(false);

        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> createByEmail(User user) {
        if (userRepository.existsByEmail(user.getUsername()))
            return Optional.empty();

        user.getNames().forEach(userLocalized -> userLocalized.setUser(user));

        if (emailChangeCodeRepository.existsByNewEmail(user.getUsername()))
            return Optional.empty();

        return Optional.ofNullable(save(user));
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailEager(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUserById(Long id) {
        return userRepository.existsById(id);
    }

}
