package ru.psuti.conf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.psuti.conf.entity.ImageFileInfo;
import ru.psuti.conf.entity.Role;
import ru.psuti.conf.entity.User;
import ru.psuti.conf.repository.EmailChangeCodeRepository;
import ru.psuti.conf.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageFileInfoService imageFileInfoService;

    private final UserRepository userRepository;

    private final EmailChangeCodeRepository emailChangeCodeRepository;

    public static Optional<User> getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
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

        if (emailChangeCodeRepository.existsByNewEmail(user.getUsername()))
            return Optional.empty();

        return Optional.ofNullable(save(user));
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
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

    public Resource getProfilePhoto(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        User user = userOptional.get();
        ImageFileInfo userImageFileInfo = user.getImageFileInfo();
        String pathToPhoto = userImageFileInfo.getName();
        if (pathToPhoto == null) {
            return null;
        }
        String uploadDir = new File("src/main/resources/static/public/photos").getAbsolutePath();
        Path filePath = Paths.get(uploadDir).resolve(pathToPhoto).normalize();
        try {
            Resource resource = resourceLoader.getResource("file:" + filePath.toString());
            if (resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ImageFileInfo uploadProfileInfo(ImageFileInfo imageFileInfo, MultipartFile multipartFile, Long userId) throws IOException {

        User user = userRepository.getById(userId);

        var image = imageFileInfoService.saveImageFileInfo(imageFileInfo, multipartFile);
        user.setImageFileInfo(image);
        userRepository.save(user);

        return image;
    }
}
