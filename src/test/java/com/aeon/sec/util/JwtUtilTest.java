package com.aeon.sec.util;

import com.aeon.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by roshane on 2/26/17.
 */
public class JwtUtilTest extends AbstractBaseTest{

    @Autowired
    private JwtUtil jwtUtil;

    public static UserDetails getUser(){
        return new User("user", "password", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void createToken() throws Exception {
        String token = jwtUtil.createToken(getUser());
        UserDetails userDetails = jwtUtil.parseToken(token);

        assertEquals(userDetails.getUsername(),getUser().getUsername());
        assertEquals(userDetails.getPassword(),getUser().getPassword());
    }

    @Test
    public void parseToken() throws Exception {

    }

}