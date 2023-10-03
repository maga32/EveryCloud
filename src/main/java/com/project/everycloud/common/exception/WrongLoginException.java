package com.project.everycloud.common.exception;

public class WrongLoginException extends RuntimeException {
    public WrongLoginException() {
        super();
    }

    public WrongLoginException(final String message) {
        super(message);
    }

    public WrongLoginException(final String message, Throwable e) {
        super(message, e);
    }
}
