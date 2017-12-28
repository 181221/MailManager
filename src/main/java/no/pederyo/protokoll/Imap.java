package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.*;
import java.util.Properties;

public class Imap implements IProtokoll {
    Properties properties;
    Session session;
    Store store;

    public Imap() {
        properties = setup();
        session = authenticate();
        store = store();
    }
    public Properties setup() {
        Properties props = System.getProperties();
        props.setProperty("mail.imap.host", Attributter.IMAPHOST);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.connectiontimeout", "5000");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.timeout", "5000");
        return props;
    }

    public Session authenticate() {
        return Session.getInstance(properties);
    }

    public Store store() {
        Store store = null;
        try {
            store = session.getStore("imap");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }
    public void connect() {
        try {
            store.connect(Attributter.IMAPHOST, Attributter.FRAMAIL, Attributter.PASSORD);
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

    public Store getStore() {
        return store;
    }
}
