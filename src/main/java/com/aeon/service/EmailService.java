package com.aeon.service;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import java.nio.file.Paths;
import java.util.Collections;

/**
 * Created by roshane on 3/4/17.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private MailSender mailSender;
    private SimpleMailMessage simpleMailMessage;
    private VelocityEngine velocityEngine;

    @Autowired
    public EmailService(MailSender mailSender, SimpleMailMessage simpleMailMessage, VelocityEngine velocityEngine) {
        Assert.notNull(mailSender,"MailSender null");
        Assert.notNull(simpleMailMessage,"SimpleMailMessage null");
        Assert.notNull(velocityEngine,"VelocityEngine null");
        this.mailSender = mailSender;
        this.simpleMailMessage = simpleMailMessage;
        this.velocityEngine = velocityEngine;
    }

    public void sendMailMessage(String message) {
        try {
            MimeMessagePreparator preparator = processMimeMessage();
            ((JavaMailSender) mailSender).send(preparator);
        } catch (MailException ex) {
            ex.printStackTrace();
            logger.error("error sending mail {}", ex);
        }
    }

    private MimeMessagePreparator processMimeMessage() {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo("roshane.perera@auxenta.com");
            helper.setFrom("spring-security-jwt");
            String s = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template.html", "UTF-8",null);
            helper.setText(s, true);
        };
        return mimeMessagePreparator;
    }


}
