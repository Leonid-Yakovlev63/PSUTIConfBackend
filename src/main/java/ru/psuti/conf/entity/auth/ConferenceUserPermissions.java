package ru.psuti.conf.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.Conference;

import java.util.Arrays;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conference_user_permissions")
public class ConferenceUserPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "conference_id", referencedColumnName = "id", nullable = false)
    private Conference conference;

    @Column(nullable = false)
    private Integer permissions = 0;

    public boolean hasAnyPermission(PermissionFlags... permissionFlags) {
        return Arrays.stream(permissionFlags).anyMatch(this::getPermission);
    }

    public boolean hasAllPermissions(PermissionFlags... permissionFlags) {
        return Arrays.stream(permissionFlags).allMatch(this::getPermission);
    }

    private boolean getPermission(PermissionFlags flag) {
        return (permissions & (1 << flag.getBit())) != 0;
    }

    private void setPermission(PermissionFlags flag, boolean value) {
        if (value) {
            permissions |= (1 << flag.getBit());
        } else {
            permissions &= ~(1 << flag.getBit());
        }
    }
}
