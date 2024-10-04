package com.Hindol.Uber;

import com.Hindol.Uber.Service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberApplicationTests {
	@Autowired
	private EmailSenderService emailSenderService;
	@Test
	void contextLoads() {
		emailSenderService
				.sendEmail("wikef21678@scarden.com",
						"This is Testing email",
						"Body of email.");
	}

}
