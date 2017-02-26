package com.aeon.sec.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roshane on 2/25/17.
 */
@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private final Map<String, User> userdetails;

    {
        userdetails = new HashMap<>();
        userdetails.put("user", new User("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("USER"))));

        userdetails.put("admin", new User("admin", "password", Arrays.asList(new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("USER"))));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loading user by user name " + username);
        if (userdetails.containsKey(username))
            return userdetails.get(username);
        throw new UsernameNotFoundException("user not found " + username);
    }
}
