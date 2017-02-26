package com.aeon.sec.ex;

import javax.security.sasl.AuthenticationException;

/**
 * Created by roshane on 2/25/17.
 */
public class JwtMissingException extends AuthenticationException {

    public JwtMissingException(String message) {
        super(message);
    }
}
