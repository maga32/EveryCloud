package com.project.everycloud.common.exception;

public class NeedLoginException extends RuntimeException {
    public NeedLoginException() {
        super();
    }

    public NeedLoginException(final String message) {
        super(message);
    }

    public NeedLoginException(final String message, Throwable e) {
        super(message, e);
    }
}
