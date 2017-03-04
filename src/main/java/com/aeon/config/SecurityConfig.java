package com.aeon.config;

import com.aeon.sec.filter.JwtAuthenticationFilter;
import com.aeon.sec.filter.StatelessJwtFilter;
import com.aeon.sec.jwt.JwtAuthenticationToken;
import com.aeon.sec.provider.JwtAuthenticationProvider;
import com.aeon.sec.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roshane on 2/25/17.
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StatelessJwtFilter statelessJwtFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    System.out.println(">>> custom authentication entry point");
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

                }))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth**").permitAll()
                .anyRequest().authenticated();


        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(statelessJwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter
                = new JwtAuthenticationFilter("/auth/login",jwtUtil,userDetailsService);

        jwtAuthenticationFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            System.out.println(">>> custom authentication success handler processing success call");

            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Map<String, Object> params = new HashMap<>();
            params.put("userDetails", jwtAuthenticationToken.getPrincipal());
            params.put("token", jwtAuthenticationToken.getToken());
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, params);
            response.getWriter().write(stringWriter.toString());
            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }));

        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenticationFilter;
    }
}
