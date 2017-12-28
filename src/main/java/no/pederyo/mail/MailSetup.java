package no.pederyo.mail;

import no.pederyo.Attributter;
import javax.mail.*;
import java.util.Properties;

public class MailSetup {
    private Properties smtpProperties;

    private Session mailSessionSmtp;

    public MailSetup() {
        smtpProperties = setUpSMTPProperties();
        mailSessionSmtp = AuthenticateSmtp();
    }

    private Properties setUpSMTPProperties() {
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.user", Attributter.FRAMAIL);
        props.put("mail.password", Attributter.PASSORD);
        props.put("mail.port", "465");
        return props;
    }

    private Session AuthenticateSmtp() {
        return Session.getInstance(this.smtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Attributter.FRAMAIL, Attributter.PASSORD);
            }
        });
    }

    public Session getMailSession() {
        return mailSessionSmtp;
    }

    public Properties getSmtpProperties() {
        return smtpProperties;
    }
}

