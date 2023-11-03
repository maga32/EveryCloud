package com.project.everycloud.common.exception;

public class CertErrorException extends RuntimeException {
    public CertErrorException() {
        super();
    }

    public CertErrorException(final String message) {
        super(message);
    }

    public CertErrorException(final String message, Throwable e) {
        super(message, e);
    }
}
