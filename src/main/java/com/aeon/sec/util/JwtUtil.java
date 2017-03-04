package com.aeon.sec.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by roshane on 2/26/17.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(UserDetails userDetails) throws UnsupportedEncodingException {
        System.out.println(">>>>> creating jwt token for " + userDetails);
        String[] authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).toArray(new String[userDetails.getAuthorities().size()]);

        try {
            return JWT.create()
                    .withClaim("user", userDetails.getUsername())
                    .withArrayClaim("authorities", authorities)
                    .withClaim("password", userDetails.getPassword())
                    .sign(Algorithm.HMAC256(secret));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UserDetails parseToken(String token) {
        System.out.println(">>>> parsing jwt token " + token);
        JWT decode = JWT.decode(token);
        String username = decode.getClaim("user").as(String.class);
        String password = decode.getClaim("password").as(String.class);
        List<SimpleGrantedAuthority> authorities = decode.getClaim("authorities").asList(String.class)
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(username, password, authorities);
    }
}
