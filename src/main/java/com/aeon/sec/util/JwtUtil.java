package com.aeon.sec.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by roshane on 2/25/17.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public User parse(String token) {

        try {
            JWT decode = JWT.decode(token);

            List<SimpleGrantedAuthority> authorities = decode.getClaims().get("authorities").asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new User(decode.getSubject(), decode.getClaim("password").asString(), authorities);
        } catch (JWTDecodeException | ClassCastException | NullPointerException ex) {
            logger.error("error decoding jwt [{}]", ex);
        }
        return null;
    }

    public String generateToken(User user) {

        String authorities[] = new String[user.getAuthorities().size()];

        user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .toArray(authorities);


        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("password", user.getPassword())
                    .withArrayClaim("authorities", authorities)
                    .sign(Algorithm.HMAC256(secret));
        } catch (UnsupportedEncodingException ex) {
            logger.error("error generating jwt {}", ex);
        }
        return null;
    }


}
