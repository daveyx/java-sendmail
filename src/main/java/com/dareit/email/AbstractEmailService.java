package com.dareit.email;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


public abstract class AbstractEmailService {

    private final Logger LOGGER;

    //@formatter:off
    private final String    host;
    private final int       port;
    private final String    user;
    private final String    password;
    //@formatter:on

    private final EmailSendService emailSendService;


    public AbstractEmailService(final String host,
                                final int port,
                                final String user,
                                final String password,
                                final EmailSendService emailSendService) {
        LOGGER = LoggerFactory.getLogger(getClass());

        this.emailSendService = emailSendService;
        Objects.requireNonNull(host);
        Objects.requireNonNull(user);
        Objects.requireNonNull(password);

        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void sendMails(final List<EmailData> mails) {
        if (mails == null || mails.isEmpty()) {
            return;
        }

        final Transport transport;
        try {
            transport = createSession();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }

        for (final EmailData emailData : mails) {
            try {
                emailSendService.sendMail(
                        transport,
                        emailData.getFromEmail(),
                        emailData.getFromName(),
                        emailData.getTo(),
                        emailData.getBcc(),
                        emailData.getSubject(),
                        emailData.getText(),
                        emailData.getHtml(),
                        emailData.getAttachmentPath());
            } catch (final Exception e) {
                LOGGER.error(e.getMessage(), e);
                closeSession(transport);
                return;
            }
        }

        closeSession(transport);
    }

    private Transport createSession() throws MessagingException {
        final Properties props = new Properties();

        if (host.contains(EmailConstants.RELAY_HOST_INDICATOR)) {
            props.put(EmailConstants.MAIL_SMTP_STARTTLS_ENABLE, Boolean.toString(true));
        } else {
            props.put(EmailConstants.MAIL_SMTP_SSL_ENABLE, Boolean.toString(true));
        }

        props.put(EmailConstants.MAIL_HOST, host);
        props.put(EmailConstants.MAIL_PORT, port);
        props.put(EmailConstants.MAIL_SMTP_AUTH, Boolean.toString(true));

        final Session session = Session.getInstance(props, null);

        final URLName urlName = new URLName(
                EmailConstants.MAIL_SMTP,
                host,
                port,
                null,
                user,
                password);

        final Transport transport = session.getTransport(urlName);
        transport.connect();

        return transport;
    }

    private void closeSession(final Transport transport) {
        if (transport == null) {
            throw new IllegalStateException("transport is null");
        }

        try {
            transport.close();
        } catch (final MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
