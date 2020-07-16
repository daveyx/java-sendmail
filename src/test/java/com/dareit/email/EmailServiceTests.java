package com.dareit.email;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource("classpath:test.properties")
class EmailServiceTests {

	//@formatter:off
	private static final String FROM_EMAIL 	= "<from-email>";
	private static final String TO_EMAIL	= "<to-email";
	//@formatter:on

	@Autowired
	private EmailService1 emailService1;

	@Autowired
	private EmailService2 emailService2;


	@Test
	void testSendMails() throws InterruptedException {
		final Pair<List<EmailData>, List<EmailData>> emailData = getEmailData();

		final ExecutorService service = Executors.newFixedThreadPool(2);
        final CountDownLatch latch = new CountDownLatch(2);

        final AtomicInteger service1sendCount = new AtomicInteger();
        service.execute(() -> {
        	service1sendCount.set(emailService1.sendMails(emailData.getLeft()));
            latch.countDown();
        });

        final AtomicInteger service2sendCount = new AtomicInteger();
        service.execute(() -> {
			service2sendCount.set(emailService2.sendMails(emailData.getRight()));
            latch.countDown();
        });

        latch.await();

        assertEquals(emailData.getLeft().size(), service1sendCount.get());
		assertEquals(emailData.getRight().size(), service2sendCount.get());
	}

	private Pair<List<EmailData>, List<EmailData>> getEmailData() {
		final List<EmailData> data1 = new ArrayList<>();
		final List<EmailData> data2 = new ArrayList<>();

		final EmailData emailData1 = new EmailData(
				FROM_EMAIL,
				null,
				TO_EMAIL,
				null,
				"testmail 1",
				"html 1",
				"text 1",
				null
		);

		final EmailData emailData2 = new EmailData(
				FROM_EMAIL,
				null,
				TO_EMAIL,
				null,
				"testmail 2",
				"html 2",
				"text 2",
				null
		);

		data1.add(emailData1);
		data2.add(emailData2);

		return Pair.of(data1, data2);
	}

}
