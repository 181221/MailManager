package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.*;
import java.util.Properties;
public class Pop3 implements IProtokoll {

    private Properties properties;
    private Store store;
    private Session session;

    public Pop3(){
        properties = setup();
        session = authenticate();
        store = Store();
    }

    public Properties setup() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", Attributter.POP3HOST);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        return properties;
    }

    public Session authenticate() {
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                Attributter.FRAMAIL, Attributter.PASSORD);
                    }
                });
    }

    public Store store() {
        return null;
    }

    public void connect() {
        try {
            store.connect(Attributter.POP3HOST,Attributter.FRAMAIL, Attributter.PASSORD);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Store Store(){
        Store store = null;
        try {
            store = session.getStore("pop3s");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

}
