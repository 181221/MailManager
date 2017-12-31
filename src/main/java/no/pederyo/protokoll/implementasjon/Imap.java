package no.pederyo.protokoll.implementasjon;

import no.pederyo.Attributter;
import no.pederyo.protokoll.IConnect;
import no.pederyo.protokoll.IProtokoll;

import javax.mail.*;
import java.util.Properties;

public class Imap implements IProtokoll, IConnect {

    private static final String  IMAP_GMAIL_COM = "imap.gmail.com",
            IMAP_OUTLOOK_COM = "imap-mail.outlook.com",
            IMAP_YAHOO_COM = "imap.mail.yahoo.com",
            IMAP_DEFAULT = IMAP_GMAIL_COM;

    private static final String[] HOST = new String[] {
            IMAP_GMAIL_COM,
            IMAP_OUTLOOK_COM,
            IMAP_YAHOO_COM};

    private Properties properties;
    private Session session;
    private Store store;
    private String mailType;

    public Imap(){
        properties = setup();
        session = authenticate();
        store = store();
    }

    public Imap(String mailType) {
        properties = setup();
        session = authenticate();
        store = store();
        this.mailType = finnHost(mailType);
    }

    public Properties setup() {
        Properties props = System.getProperties();
        props.setProperty("mail.imap.host", mailType != null ? mailType : IMAP_DEFAULT);
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
            store.connect(mailType, Attributter.FRAMAIL, Attributter.PASSORD);
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

    public Folder getFolder(String mappe){
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

    private static String finnHost(String sokestreng) {
        for (String h : HOST) {
            if(h != null){
                if(h.equals(sokestreng)){
                    return h;
                }
            }
        }
        return null;
    }

    public Folder opprettFolder(String navn){
        Folder nyfolder = null;
        try {
            nyfolder = store.getFolder(navn);
            nyfolder.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return nyfolder;
    }


}
