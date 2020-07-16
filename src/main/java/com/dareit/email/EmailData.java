package com.dareit.email;


public class EmailData {

    private final String fromEmail;
    private final String fromName;
    private final String to;
    private final String bcc;
    private final String subject;
    private final String html;
    private final String text;
    private final String attachmentPath;


    public EmailData(String fromEmail,
                     String fromName,
                     String to,
                     String bcc,
                     String subject,
                     String html,
                     String text,
                     String attachmentPath) {
        this.fromEmail = fromEmail;
        this.fromName = fromName;
        this.to = to;
        this.bcc = bcc;
        this.subject = subject;
        this.html = html;
        this.text = text;
        this.attachmentPath = attachmentPath;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public String getTo() {
        return to;
    }

    public String getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getHtml() {
        return html;
    }

    public String getText() {
        return text;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

}
