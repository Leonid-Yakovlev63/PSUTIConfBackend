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

    @Column(nullable = false)
    private Boolean emailVerified;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(name = "psuti_username")
    private String psutiUsername;

    @Column(nullable = false)
    private String password;

    @Column(name = "firstname_ru", length = 50, nullable = false)
    private String firstnameRu;

    @Column(name = "firstname_en", length = 50)
    private String firstnameEn;

    @Column(name = "lastname_ru", length = 100, nullable = false)
    private String lastnameRu;

    @Column(name = "lastname_en", length = 100)
    private String lastnameEn;

    @Column(name = "middlename_ru", length = 50)
    private String middlenameRu;

    @Column(name = "middlename_en", length = 50)
    private String middlenameEn;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "city", length = 50)
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

    @Override
    public boolean isEnabled() {
        return getEmailVerified();
    }
}
