package com.aeon.sec.provider;

import com.aeon.sec.ex.JwtCreationException;
import com.aeon.sec.jwt.JwtAuthenticationToken;
import com.aeon.sec.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Created by roshane on 2/27/17.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) jwtAuthenticationToken.getPrincipal());
        System.out.println(">>> JwtAuthenticationProvider authenticating");
        if (userDetails == null || !jwtAuthenticationToken.getCredentials().equals(userDetails.getPassword())) {
            throw new BadCredentialsException("invalid credentials");
        }
        try {
            return new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities(), jwtUtil.createToken(userDetails));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new JwtCreationException("error creating jwt token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
