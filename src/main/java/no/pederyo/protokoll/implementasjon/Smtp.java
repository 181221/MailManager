package no.pederyo.protokoll.implementasjon;

import no.pederyo.Attributter;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.IProtokoll;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Properties;

public class Smtp implements IProtokoll {
    public static final String SMTP_GMAIL_COM = "smtp.gmail.com",
            SMTP_OUTLOOK_COM = "smtp-mail.outlook.com",
            SMTP_YAHOO_COM = "smtp.mail.yahoo.com",
            SMTP_DEFAULT = SMTP_GMAIL_COM;

    public static final HashMap<Integer, String> HOSTMAP = new HashMap<>();

    private String mailType;
    private Properties properties;
    private Session session;

    public Smtp(){
        properties = setup();
        session = authenticate();
    }

    public Smtp(int mailType){
        leggTilalle();
        this.mailType = HOSTMAP.get(mailType);
        properties = setup();
        session = authenticate();
    }

    public Properties setup() {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailType != null ? mailType : SMTP_DEFAULT);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.user", Attributter.FRAMAIL);
        props.put("mail.password", Attributter.PASSORD);
        props.put("mail.port", "465");
        return props;

    }

    public Session authenticate() {
        return Session.getInstance(this.properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Attributter.FRAMAIL, Attributter.PASSORD);
            }
        });
    }

    public String send(Mail mail) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFra()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTil()));
            message.setSubject(mail.getSubject());
            message.setText(mail.getMsg());
            javax.mail.Transport.send(message);
            mail.setResult("Your mail sent successfully....");
        } catch (AddressException e) {
            mail.setResult(e.getMessage());
        } catch (javax.mail.MessagingException e) {
            mail.setResult(e.getMessage());
        }
        return mail.getResult();
    }

    private void leggTilalle(){
        HOSTMAP.put(0, SMTP_GMAIL_COM);
        HOSTMAP.put(1, SMTP_OUTLOOK_COM);
        HOSTMAP.put(2, SMTP_YAHOO_COM);
    }
}
