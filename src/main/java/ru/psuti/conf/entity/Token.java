package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.auth.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @Column(nullable = false, length = 512)
    private String token;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expires;
}
