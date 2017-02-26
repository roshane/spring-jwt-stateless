package com.aeon.sec.util;

import com.aeon.AbstractBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by roshane on 2/25/17.
 */
public class JwtUtilTest extends AbstractBaseTest {

    private final User user = new User("user", "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    private String sign;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Before
    public void init() throws Exception {
        sign = jwtUtil.generateToken(user);
    }

    @Test
    public void parse() throws Exception {
        User parse = jwtUtil.parse(sign);
        assertEquals(parse.getUsername(), "user");
        assertEquals(parse.getPassword(), "password");

        assertNotNull(parse.getAuthorities());
        assertTrue(parse.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void generateToken() throws Exception {
        assertEquals(sign,jwtUtil.generateToken(user));
    }

}