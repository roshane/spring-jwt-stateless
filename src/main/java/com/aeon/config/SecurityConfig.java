package com.aeon.config;

import com.aeon.sec.ApiAuthenticationEntryPoint;
import com.aeon.sec.filter.JwtAuthenticationFilter;
import com.aeon.sec.handlers.ApiAuthenticationFailureHandler;
import com.aeon.sec.handlers.ApiAuthenticationSuccessHandler;
import com.aeon.sec.provider.JwtAuthenticationProvider;
import com.aeon.sec.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by roshane on 2/25/17.
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApiAuthenticationSuccessHandler successHandler;

    @Autowired
    private ApiAuthenticationFailureHandler failureHandler;

    @Autowired
    private ApiAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        http
                .authorizeRequests()
                .antMatchers("/api**").hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .userDetailsService(userDetailsService);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().cacheControl();
    }
}
