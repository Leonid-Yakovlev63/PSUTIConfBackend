package ru.psuti.conf.entity.auth;

import lombok.Getter;

@Getter
public enum PermissionFlags {
    READ(0),
    WRITE(1),
    ADMIN(3),
    READ_HIDDEN_PAGES(4),
    WRITE_PAGES(5),
    READ_CONF_APP(6),
    CREATE_CONF_APP(7),
    ACCEPT_CONF_APP(8),
    READ_DATE_CONF_APP(9),
    EDIT_DATE_CONF_APP(10),
    READ_CONTENT_CONF_APP(11),
    EDIT_CONTENT_CONF_APP(12);

    private final int bit;

    PermissionFlags(int bit) {
        this.bit = bit;
    }

}
