package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "psuti_account_binding_code")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsutiAccountBindingCode {

    @Id
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "psuti_username", nullable = false)
    private String psutiUsername;

    @Column(name = "firstname_ru", length = 50, nullable = false)
    private String firstnameRu;

    @Column(name = "surname_ru", length = 100, nullable = false)
    private String surnameRu;

    @Column(name = "lastname_ru", length = 50)
    private String lastnameRu;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expires;

}
