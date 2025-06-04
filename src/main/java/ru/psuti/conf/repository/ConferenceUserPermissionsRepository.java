package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.psuti.conf.entity.Conference;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;
import ru.psuti.conf.entity.auth.User;

import java.util.Optional;
import java.util.UUID;

public interface ConferenceUserPermissionsRepository extends JpaRepository<ConferenceUserPermissions, Long> {

    public int deleteByConference_SlugAndUser_Id(String slug, UUID id);

    public Optional<ConferenceUserPermissions> findByConference_SlugAndUser(String slug, User user);

}
