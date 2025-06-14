package ru.psuti.conf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.psuti.conf.entity.auth.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.conferenceUserPermissions WHERE u.email = :email")
    Optional<User> findByEmailEager(@Param("email") String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long deleteByCreatedAtBeforeAndEmailVerifiedFalse(LocalDateTime createdAt);
}
