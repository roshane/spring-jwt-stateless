package com.aeon.sec.ex;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by roshane on 2/27/17.
 */
public class JwtCreationException extends AuthenticationException {

    public JwtCreationException(String msg) {
        super(msg);
    }
}
