package com.project.everycloud.common.exception;

public class CertSiteErrorException extends RuntimeException {
    public CertSiteErrorException() {
        super();
    }

    public CertSiteErrorException(final String message) {
        super(message);
    }

    public CertSiteErrorException(final String message, Throwable e) {
        super(message, e);
    }
}
