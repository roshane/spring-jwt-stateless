package com.aeon.sec.filter;

import com.aeon.sec.ex.JwtMissingException;
import com.aeon.sec.jwt.JwtAuthenticationToken;
import com.aeon.sec.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by roshane on 2/27/17.
 */
@Component("statelessJwtFilter")
public class StatelessJwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private static final Logger logger = LoggerFactory.getLogger(StatelessJwtFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(">>>> StatelessJwtFilter checking jwt header");
        logger.debug(">>>> StatelessJwtFilter checking jwt header");
        String jwt = obtainJwt(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }


        UserDetails userDetails = jwtUtil.parseToken(jwt);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(userDetails, null   , userDetails.getAuthorities(), jwt);
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        logger.debug("stateless filter granted authorities {}", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }

    private static String obtainJwt(HttpServletRequest request) {
        if (StringUtils.hasText(request.getHeader(AUTHORIZATION))) {
            String jwtHeader = request.getHeader(AUTHORIZATION);
            System.out.println(">>> StatelessJwtFilter found jwt header");
            logger.debug(">>> StatelessJwtFilter found jwt header");
            return jwtHeader.substring(BEARER.length(), jwtHeader.length());
        }
        return null;
    }
}
