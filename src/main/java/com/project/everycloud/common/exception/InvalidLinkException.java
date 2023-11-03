package com.project.everycloud.common.exception;

public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException() {
        super();
    }

    public InvalidLinkException(final String message) {
        super(message);
    }

    public InvalidLinkException(final String message, Throwable e) {
        super(message, e);
    }
}
