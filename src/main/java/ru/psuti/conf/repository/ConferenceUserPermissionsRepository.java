package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.psuti.conf.entity.auth.ConferenceUserPermissions;

public interface ConferenceUserPermissionsRepository extends JpaRepository<ConferenceUserPermissions, Long> {

}
