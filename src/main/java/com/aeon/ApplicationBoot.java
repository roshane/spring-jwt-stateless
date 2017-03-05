package com.aeon;

import com.aeon.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApplicationBoot {

//	@Autowired
//	private EmailService emailService;
//
//	{
//		Runtime.getRuntime()
//				.addShutdownHook(new Thread(() -> {
//					emailService.sendMailMessage("Application shutting down");
//				}));
//	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationBoot.class, args);
	}
}
