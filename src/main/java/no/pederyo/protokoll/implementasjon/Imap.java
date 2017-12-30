package no.pederyo.protokoll.implementasjon;

import no.pederyo.Attributter;
import no.pederyo.protokoll.IConnect;
import no.pederyo.protokoll.IProtokoll;

import javax.mail.*;
import java.util.Properties;

public class Imap implements IProtokoll, IConnect {
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
        props.setProperty("mail.imap.host", Attributter.IMAP_GMAIL_COM);
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
            store.connect(Attributter.IMAP_GMAIL_COM, Attributter.FRAMAIL, Attributter.PASSORD);
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
    public Folder getMappe(String mappe){
        Folder folder = null;
        if(mappe != null){
            try {
                folder = store.getFolder(mappe);
            }catch (MessagingException e){
                e.printStackTrace();
            }
            close();
        }
        return folder;
    }
    public void checkInbox() {
        try {
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            System.out.println("Antall meldinger " + inbox.getMessageCount());
            System.out.println("Nye meldinger " + inbox.getUnreadMessageCount());
            close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
