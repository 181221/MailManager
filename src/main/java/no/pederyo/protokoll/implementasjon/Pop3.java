package no.pederyo.protokoll.implementasjon;

import no.pederyo.Attributter;
import no.pederyo.protokoll.IConnect;
import no.pederyo.protokoll.IProtokoll;

import javax.mail.*;
import java.util.Properties;
public class Pop3 implements IProtokoll, IConnect {
    private static final String POP_GMAIL_COM = "pop.gmail.com";

    private Properties properties;
    private Store store;
    private Session session;

    public Pop3(){
        properties = setup();
        session = authenticate();
        store = store();
    }

    public Properties setup() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", POP_GMAIL_COM);
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

    public boolean connect() {
        try {
            store.connect(POP_GMAIL_COM,Attributter.FRAMAIL, Attributter.PASSORD);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return store().isConnected();
    }

    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public Store store(){
        Store store = null;
        try {
            store = session.getStore("pop3s");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }
}
