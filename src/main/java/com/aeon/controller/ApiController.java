package com.aeon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by roshane on 2/25/17.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping("/current-user")
    public ResponseEntity<UserDetails> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("current user details [{}]", userDetails);
        return ResponseEntity.ok(userDetails);
    }

    @RequestMapping("/ping")
    public ResponseEntity<?> ping() {
        logger.debug("ping response {[]}", "pong");
        return ResponseEntity.ok("pong");
    }

    @RequestMapping("/admin/current-user")
    public ResponseEntity<UserDetails> adminUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("admin current-user ");
        return ResponseEntity.ok(userDetails);
    }
}
