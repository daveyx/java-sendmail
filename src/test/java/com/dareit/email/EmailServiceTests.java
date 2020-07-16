package com.dareit.email;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestPropertySource("classpath:test.properties")
class EmailServiceTests {

	@Autowired
	private EmailService1 emailService1;


	@Test
	void contextLoads() {
		final Pair<List<EmailData>, List<EmailData>> emailData = getEmailData();
		emailService1.sendMails(emailData.getLeft());
	}

	private Pair<List<EmailData>, List<EmailData>> getEmailData() {
		final List<EmailData> data1 = new ArrayList<>();
		final List<EmailData> data2 = new ArrayList<>();


		return Pair.of(data1, data2);
	}

}
