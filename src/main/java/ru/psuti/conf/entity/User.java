package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(name = "psuti_username")
    private String psutiUsername;

    private String password;

    @Column(name = "first_name_ru", length = 50, nullable = false)
    private String firstnameRu;

    @Column(name = "first_name_en", length = 50)
    private String firstnameEn;

    @Column(name = "surname_ru", length = 100, nullable = false)
    private String surnameRu;

    @Column(name = "surname_en", length = 100)
    private String surnameEn;

    @Column(name = "lastname_ru", length = 50)
    private String lastnameRu;

    @Column(name = "lastname_en", length = 50)
    private String lastnameEn;

    @Column(name = "country", length = 32, nullable = false)
    private String country;

    @Column(name = "city", length = 32, nullable = false)
    private String city;

    @Column(name = "organization")
    private String organization;

    @Column(name = "organization_address")
    private String organizationAddress;

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
}
