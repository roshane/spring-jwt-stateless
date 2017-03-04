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
@Service("userDetailsService")
public class InMemoryUserDetailsService implements UserDetailsService {

    private static final Map<String, UserDetails> users;

    static {
        users = new HashMap<>();

        users.put("user", new User("user", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ACTUATOR"))));

        users.put("admin", new User("admin", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ACTUATOR"))));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(String.format("looking up for user %s",username));
        if (users.containsKey(username)) {
            return copyUser(users.get(username));
        }
        throw new UsernameNotFoundException(String.format("user not found %s", username));
    }

    public static UserDetails copyUser(UserDetails user){
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
