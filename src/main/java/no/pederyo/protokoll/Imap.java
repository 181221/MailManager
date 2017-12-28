package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
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
        Properties props = new Properties();
        props.setProperty("mail.imaps.host", Attributter.IMAPHOST);
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", "5000");
        props.setProperty("mail.imaps.timeout", "5000");
        return props;
    }

    public Session authenticate() {
        return Session.getDefaultInstance(properties, null);
    }

    public Store store() {
        Store store = null;
        try {
            store = session.getStore("imaps");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }
    public void connect() {
        try {
            store.connect(Attributter.IMAPHOST,Attributter.FRAMAIL, Attributter.PASSORD);
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
}
