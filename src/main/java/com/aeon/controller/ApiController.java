package com.aeon.controller;

import com.aeon.metrics.ApiHitCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by roshane on 2/25/17.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiHitCounter apiHitCounter;


    @Cacheable(value = "api-cache", keyGenerator = "cacheKeyGen")
    @RequestMapping("/current-user")
    public ResponseEntity<UserDetails> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("current user details [{}]", userDetails);
        apiHitCounter.incrementApiHit(String.format("%s.%s", ApiController.class.getName(), "get-current-user"));
        return ResponseEntity.ok(userDetails);
    }

    @Cacheable(value = "api-cache", keyGenerator = "cacheKeyGen")
    @RequestMapping("/ping")
    public ResponseEntity<?> ping(@RequestParam(required = false, value = "name") String name) {
        logger.debug("ping response [{}]", "pong");
        apiHitCounter.incrementApiHit(String.format("%s.%s", ApiController.class.getName(), "ping"));
        return ResponseEntity.ok("pong");
    }

    @Cacheable(value = "api-cache", keyGenerator = "cacheKeyGen")
    @RequestMapping("/admin/current-user")
    public ResponseEntity<UserDetails> adminUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("admin current-user ");
        apiHitCounter.incrementApiHit(String.format("%s.%s", ApiController.class.getName(), "admin-user-details "));
        return ResponseEntity.ok(userDetails);
    }
}
