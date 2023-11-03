package com.project.everycloud.common.exception;

public class ExistNameException extends RuntimeException {
    public ExistNameException() {
        super();
    }

    public ExistNameException(final String message) {
        super(message);
    }

    public ExistNameException(final String message, Throwable e) {
        super(message, e);
    }
}
