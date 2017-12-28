package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class Smtp implements IProtokoll {

    private Properties properties;
    private Session session;

    public Smtp(){
        properties = setup();
        session = authenticate();
    }
    public Properties setup() {
        Properties props = new Properties();
        props.put("mail.smtp.host", Attributter.SMTPHOST);
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

    public Store store() {
        return null;
    }

    public void connect() {

    }

    public void close() {

    }

    public Session getSession() {
        return session;
    }
}
