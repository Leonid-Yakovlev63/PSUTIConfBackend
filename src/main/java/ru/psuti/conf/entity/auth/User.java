package ru.psuti.conf.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.psuti.conf.entity.Locale;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role = Role.USER;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Locale preferredLocale = Locale.EN;

    @Column(name = "psuti_username")
    private String psutiUsername;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLocalized> names;

    @Column(name = "country", length = 300)
    private String country;

    @Column(name = "city", length = 300)
    private String city;

    @Column(name = "organization", length = 900)
    private String organization;

    @Column(name = "organization_address", length = 450)
    private String organizationAddress;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ConferenceUserPermissions> conferenceUserPermissions;

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
