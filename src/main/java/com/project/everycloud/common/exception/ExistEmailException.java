package com.project.everycloud.common.exception;

public class ExistEmailException extends RuntimeException {
    public ExistEmailException() {
        super();
    }

    public ExistEmailException(final String message) {
        super(message);
    }

    public ExistEmailException(final String message, Throwable e) {
        super(message, e);
    }
}
