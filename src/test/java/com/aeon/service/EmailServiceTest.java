package com.aeon.service;

import com.aeon.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by roshane on 3/4/17.
 */
public class EmailServiceTest extends AbstractBaseTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void contextLoaded() throws Exception {
        assertNotNull(emailService);
    }

    @Test
    public void sendMailMessage() throws Exception {
        emailService.sendMailMessage("Hello There how are you");
    }

    @Test
    public void loadMailTemplate() throws Exception {
        ClassPathResource template = new ClassPathResource("template.html");
        System.out.println(">>> "+Paths.get(template.getURI()).toString());
        assertTrue(template.exists());
    }
}