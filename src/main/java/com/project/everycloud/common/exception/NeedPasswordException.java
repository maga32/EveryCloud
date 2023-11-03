package com.project.everycloud.common.exception;

public class NeedPasswordException extends RuntimeException {
    public NeedPasswordException() {
        super();
    }

    public NeedPasswordException(final String message) {
        super(message);
    }

    public NeedPasswordException(final String message, Throwable e) {
        super(message, e);
    }
}
