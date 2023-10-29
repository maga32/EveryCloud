package com.project.everycloud.common.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException() {
        super();
    }

    public NotAllowedException(final String message) {
        super(message);
    }

    public NotAllowedException(final String message, Throwable e) {
        super(message, e);
    }
}
