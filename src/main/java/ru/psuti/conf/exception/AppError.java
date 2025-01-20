package ru.psuti.conf.exception;

import java.util.Date;

public class AppError<T> {

    private Short status;

    private T message;

    private Date timestamp;

    public AppError(Short status, T message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
