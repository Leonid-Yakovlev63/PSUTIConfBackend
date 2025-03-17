package ru.psuti.conf.entity.confirms;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.auth.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_code")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetCode {

    @Id
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expires;

}
