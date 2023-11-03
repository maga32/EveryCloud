package com.project.everycloud.common.exception;

public class NeedAdminException extends RuntimeException {
    public NeedAdminException() {
        super();
    }

    public NeedAdminException(final String message) {
        super(message);
    }

    public NeedAdminException(final String message, Throwable e) {
        super(message, e);
    }
}
