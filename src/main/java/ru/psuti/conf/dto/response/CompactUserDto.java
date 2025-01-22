package ru.psuti.conf.dto.response;

import lombok.Value;
import ru.psuti.conf.entity.Role;
import ru.psuti.conf.entity.User;

/**
 * DTO for {@link ru.psuti.conf.entity.User}
 */
@Value
public class CompactUserDto {
    Long id;
    String email;
    Role role;
    String psutiUsername;
    String firstnameRu;
    String firstnameEn;
    String lastnameRu;
    String lastnameEn;
    String middlenameRu;
    String middlenameEn;

    public CompactUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.psutiUsername = user.getPsutiUsername();
        this.firstnameRu = user.getFirstnameRu();
        this.firstnameEn = user.getFirstnameEn();
        this.lastnameRu = user.getLastnameRu();
        this.lastnameEn = user.getLastnameEn();
        this.middlenameRu = user.getMiddlenameRu();
        this.middlenameEn = user.getMiddlenameEn();
    }
}