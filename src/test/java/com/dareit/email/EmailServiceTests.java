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

	//@formatter:off
	private static final String FROM_EMAIL 	= "<from-email>";
	private static final String TO_EMAIL	= "<to-email";
	//@formatter:on

	@Autowired
	private EmailService1 emailService1;


	@Test
	void testSendMails() {
		final Pair<List<EmailData>, List<EmailData>> emailData = getEmailData();
		emailService1.sendMails(emailData.getLeft());
	}

	private Pair<List<EmailData>, List<EmailData>> getEmailData() {
		final List<EmailData> data1 = new ArrayList<>();
		final List<EmailData> data2 = new ArrayList<>();

		final EmailData emailData1 = new EmailData(
				FROM_EMAIL,
				null,
				TO_EMAIL,
				null,
				"testmail",
				"html",
				"text",
				null
		);

		data1.add(emailData1);

		return Pair.of(data1, data2);
	}

}
