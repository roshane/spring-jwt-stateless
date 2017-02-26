package com.aeon.sec.service;

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
 * Created by roshane on 2/26/17.
 */
@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private static final Map<String, UserDetails> users;

    static {
        users = new HashMap<>();
        users.put("user", new User("user", "password", Collections.singletonList(new SimpleGrantedAuthority("USER"))));
        users.put("admin", new User("admin", "password", Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER"))));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(String.format("looking up for user %s",username));
        if (users.containsKey(username)) {
            UserDetails userDetails = users.get(username);
            System.out.println("found the user "+userDetails);
            return userDetails;
        }
        throw new UsernameNotFoundException(String.format("user not found %s", username));
    }
}
