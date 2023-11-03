package com.project.everycloud.common.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super();
    }

    public InvalidLoginException(final String message) {
        super(message);
    }

    public InvalidLoginException(final String message, Throwable e) {
        super(message, e);
    }
}
