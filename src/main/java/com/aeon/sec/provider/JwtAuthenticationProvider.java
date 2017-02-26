package com.aeon.sec.provider;

import com.aeon.sec.JwtAuthenticationToken;
import com.aeon.sec.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by roshane on 2/25/17.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationProvider(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = ((JwtAuthenticationToken) authentication);
        String username = jwtAuthenticationToken.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            return new JwtAuthenticationToken(username, userDetails.getPassword(),
                    userDetails.getAuthorities(), jwtUtil.generateToken((User) userDetails));
        }
        throw new UsernameNotFoundException("username not found " + username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
