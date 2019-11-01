package io.skymind.pathmind.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailIsNotVerifiedException extends AuthenticationException {
    public EmailIsNotVerifiedException(String msg) {
        super(msg);
    }

    public EmailIsNotVerifiedException(String msg, Throwable t) {
        super(msg, t);
    }
}
