package com.project.everycloud.common.exception;

public class ExistIdException extends RuntimeException {
    public ExistIdException() {
        super();
    }

    public ExistIdException(final String message) {
        super(message);
    }

    public ExistIdException(final String message, Throwable e) {
        super(message, e);
    }
}
