package com.dareit.email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;


@Service
public class EmailSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSendService.class);


    void sendMail(final Transport transport,
                  final String fromEmail,
                  final String fromName,
                  final String toEmail,
                  final String bccEmail,
                  final String subject,
                  final String text,
                  final String html,
                  final String attachmentPath) throws IOException, MessagingException {
        LOGGER.info("sending mail to '" + toEmail + "'");

        final InternetAddress from = new InternetAddress(fromEmail, fromName);
        from.validate();

        final InternetAddress to = new InternetAddress(toEmail);
        to.validate();

        final InternetAddress bcc;

        if (StringUtils.isNotBlank(bccEmail)) {
            bcc = new InternetAddress(bccEmail);
            bcc.validate();
        } else {
            bcc = null;
        }

        final MimeMessage mimeMessage = getEmail(
                from,
                to,
                bcc,
                subject,
                text,
                html,
                attachmentPath);

        send(transport, mimeMessage);
    }

    private void send(final Transport transport, final MimeMessage mimeMessage) throws MessagingException {
        final Address[] addresses = mimeMessage.getAllRecipients();

        if (addresses == null ) {
            throw new IllegalStateException("no recipient");
        }

        transport.sendMessage(mimeMessage, addresses);
    }

    private MimeMessage getEmail(final InternetAddress from,
                                 final InternetAddress to,
                                 final InternetAddress bcc,
                                 final String subject,
                                 final String text,
                                 final String html,
                                 final String attachmentPath) throws MessagingException, IOException {
        final Session session = null;

        @SuppressWarnings("ConstantConditions")
        final MimeMessage message = new MimeMessage(session);

        message.setFrom(from);
        message.setSubject(subject);
        message.setRecipient(Message.RecipientType.TO, to);

        if (bcc != null) {
            message.setRecipient(Message.RecipientType.BCC, bcc);
        }

        message.setContent(buildMultipart(text, html, attachmentPath));

        return message;
    }

    private Multipart buildMultipart(final String messageText,
                                     final String messageHtml,
                                     final String attachmentPath) throws MessagingException, IOException {
        final Multipart multipartMixed = new MimeMultipart("mixed");
        final Multipart multipartAlternative = newChild(multipartMixed, "alternative");

        addTextVersion(multipartAlternative, messageText);
        addHtmlVersion(multipartAlternative, messageHtml);
        addAttachment(multipartMixed, attachmentPath);

        return multipartMixed;
    }

    private Multipart newChild(final Multipart parent, final String alternative) throws MessagingException {
        final MimeMultipart child =  new MimeMultipart(alternative);
        final MimeBodyPart mimeBodyPart = new MimeBodyPart();
        parent.addBodyPart(mimeBodyPart);
        mimeBodyPart.setContent(child);
        return child;
    }

    private void addTextVersion(final Multipart multipart, final String messageText) throws MessagingException {
        final MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(messageText, "text/plain");
        multipart.addBodyPart(textBodyPart);
    }

    private void addHtmlVersion(final Multipart parent, final String messageHtml) throws MessagingException {
        final Multipart multipart = newChild(parent,"related");
        final MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(messageHtml, "text/html");
        multipart.addBodyPart(htmlBodyPart);
    }

    private void addAttachment(final Multipart parent, final String attachmentPath) throws MessagingException, IOException {
        if (StringUtils.isNotBlank(attachmentPath)) {
            final MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(attachmentPath));
            attachmentBodyPart.setDisposition(BodyPart.ATTACHMENT);
            parent.addBodyPart(attachmentBodyPart);
        }
    }

}
