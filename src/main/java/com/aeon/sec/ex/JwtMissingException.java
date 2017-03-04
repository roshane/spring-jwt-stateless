package com.aeon.sec.ex;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by roshane on 2/26/17.
 */
public class JwtMissingException extends AuthenticationException {

    public JwtMissingException(String msg) {
        super(msg);
    }
}
