package ru.psuti.conf.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.Locale;

@Entity
@Table(name = "users_localized")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocalized {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Locale locale;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(length = 100)
    private String firstName;

    @Column(length = 150)
    private String lastName;

    @Column(length = 100)
    private String middleName;

}
