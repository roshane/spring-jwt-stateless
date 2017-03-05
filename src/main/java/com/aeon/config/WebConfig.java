package com.aeon.config;

import com.aeon.service.ApiCacheKeyGenerator;
import com.google.common.cache.CacheBuilder;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by roshane on 2/25/17.
 */
@Configuration
@EnableWebMvc
@EnableCaching
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable", "true");
        javaMailSender.setJavaMailProperties(props);

        javaMailSender.setPort(465);
        javaMailSender.setUsername("kadirabanda28@gmail.com");
        javaMailSender.setPassword("507509kadira50");
        return javaMailSender;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("roshane.perera@auxenta.com");
        simpleMailMessage.setSubject("Test Mail");
        return simpleMailMessage;
    }

    @Bean
    public VelocityEngine velocityEngine() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader","class");
        properties.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityEngine(properties);
    }

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager("application-cache");
        guavaCacheManager.setAllowNullValues(false);
        guavaCacheManager.setCacheBuilder(
                CacheBuilder.newBuilder()
                        .maximumSize(Integer.MAX_VALUE)
                        .removalListener((notification -> {
                            logger.debug("cache remove event key [{}]", notification.getKey());
                        }))
                        .expireAfterWrite(10, TimeUnit.SECONDS  )
        );
        guavaCacheManager.setCacheNames(Collections.singletonList("api-cache"));
        return guavaCacheManager;
    }

    @Bean(value = "cacheKeyGen")
    public KeyGenerator keyGenerator() {
        return new ApiCacheKeyGenerator();
    }
}
