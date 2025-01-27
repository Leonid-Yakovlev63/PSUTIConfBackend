package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_change_code")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailChangeCode {

    @Id
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @Column(name = "new_email", nullable = false)
    private String newEmail;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expires;

}
