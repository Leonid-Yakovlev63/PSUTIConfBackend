package ru.psuti.conf.entity.auth;

import lombok.Getter;

@Getter
public enum PermissionFlags {
    READ(0), // Чтение закрытой конференций
    WRITE(1), // Изменение конференций
    ADMIN(3),
    READ_HIDDEN_PAGES(4), // Чтение страниц, которые не отображаются обычным пользователям
    WRITE_PAGES(5), // Изменять страницы
    READ_CONF_APP(6), // Чтение заявок
    CREATE_CONF_APP(7), // Создание заявок
    ACCEPT_CONF_APP(8), // Изменение статуса заявок
    READ_DATE_CONF_APP(9), // Чтение дат
    EDIT_DATE_CONF_APP(10), // Изменение дат
    READ_CONTENT_CONF_APP(11), // Чтение содержимого
    EDIT_CONTENT_CONF_APP(12); // Изменение содержимого

    private final int bit;

    PermissionFlags(int bit) {
        this.bit = bit;
    }

}
