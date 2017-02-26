package com.aeon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

/**
 * Created by roshane on 2/25/17.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                System.out.println("custom authentication provider attempt authentication " + authentication);

                UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) authentication);
                UserDetails userDetails = userDetailsService.loadUserByUsername((String) token.getPrincipal());
                if (userDetails != null && userDetails.getUsername().equals(token.getPrincipal())) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    return auth;
                }

                throw new BadCredentialsException(
                        String.format("invalid credentials provided for user %s", token.getPrincipal().toString())
                );
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        }).eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    StringWriter stringWriter = new StringWriter();
                    Map<String, String> respBody = Collections.singletonMap("message", authException.getMessage());
                    try {
                        objectMapper.writeValue(stringWriter, respBody);
                        response.getWriter().write(stringWriter.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }));

        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginProcessingUrl("/login").permitAll()
                .successHandler(((request, response, authentication) -> {
                    StringWriter sw = new StringWriter();
                    objectMapper.writeValue(sw, authentication.getPrincipal());
                    response.getWriter().write(sw.toString());
                    response.setStatus(HttpStatus.OK.value());
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }))
                .and()
                .httpBasic();
    }
}
