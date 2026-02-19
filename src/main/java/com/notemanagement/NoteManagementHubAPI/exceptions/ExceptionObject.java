package com.notemanagement.NoteManagementHubAPI.exceptions;

import org.springframework.http.HttpStatus;

public class ExceptionObject {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public ExceptionObject(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
