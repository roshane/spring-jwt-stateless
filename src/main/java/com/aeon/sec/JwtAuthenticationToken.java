package com.aeon.sec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by roshane on 2/25/17.
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
        super(principal, credentials, authorities);
        this.token = token;
    }

    public JwtAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
