package com.aeon.sec.filter;

import com.aeon.sec.ex.JwtMissingException;
import com.aeon.sec.jwt.JwtAuthenticationToken;
import com.aeon.sec.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by roshane on 2/26/17.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private JwtUtil jwtUtil;

    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username=getUsername(request);
        String password=getPassword(request);

        System.out.println(">>>> JwtAuthenticationFilter attempt authentication for user "+username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new UsernameNotFoundException("user name not found for user "+username);
        }
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities(), null);
        return getAuthenticationManager().authenticate(jwtAuthenticationToken);
    }

    private static String getUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    private static String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
