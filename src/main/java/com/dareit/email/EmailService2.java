package com.dareit.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailService2 extends AbstractEmailService {


    public EmailService2(@Value("${" + EmailConstants.MAIL_HOST + "}") final String host,
                         @Value("${" + EmailConstants.MAIL_PORT + "}") final String port,
                         @Value("${" + EmailConstants.MAIL_USER + "}") final String user,
                         @Value("${" + EmailConstants.MAIL_PASSWORD + "}") final String password,
                         final EmailSendService emailSendService) {
        super(
                host,
                Integer.parseInt(port),
                user,
                password,
                emailSendService);
    }

}
