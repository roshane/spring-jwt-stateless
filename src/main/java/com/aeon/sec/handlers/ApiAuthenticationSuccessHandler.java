package com.aeon.sec.handlers;

import com.aeon.sec.JwtAuthenticationToken;
import com.aeon.sec.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roshane on 2/25/17.
 */
@Component
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("onAuthenticationSuccess authentication " + authentication);
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authenticationToken = ((JwtAuthenticationToken) authentication);
            String token = authenticationToken.getToken();
            Map<String, Object> params = new HashMap<>();

            User user = jwtUtil.parse(token);
            params.put("userDetails", user);
            params.put("jwt", token);

            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, params);
            System.out.println("json response " + stringWriter.toString());
            response.getWriter().write(stringWriter.toString());
        }
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
