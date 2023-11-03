package com.project.everycloud.common.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(final String message) {
        super(message);
    }

    public InvalidPasswordException(final String message, Throwable e) {
        super(message, e);
    }
}
