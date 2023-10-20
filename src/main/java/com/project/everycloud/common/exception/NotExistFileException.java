package com.project.everycloud.common.exception;

public class NotExistFileException extends RuntimeException {
    public NotExistFileException() {
        super();
    }

    public NotExistFileException(final String message) {
        super(message);
    }

    public NotExistFileException(final String message, Throwable e) {
        super(message, e);
    }
}
