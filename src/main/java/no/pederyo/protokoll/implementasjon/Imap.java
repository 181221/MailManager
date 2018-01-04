package no.pederyo.protokoll.implementasjon;

import no.pederyo.Attributter;
import no.pederyo.protokoll.IConnect;
import no.pederyo.protokoll.IImap;
import no.pederyo.protokoll.IProtokoll;

import javax.mail.*;
import java.util.HashMap;
import java.util.Properties;

public class Imap implements IProtokoll, IConnect, IImap {

    public static final String  IMAP_GMAIL_COM = "imap.gmail.com",
            IMAP_OUTLOOK_COM = "imap-mail.outlook.com",
            IMAP_YAHOO_COM = "imap.mail.yahoo.com",
            IMAP_DEFAULT = IMAP_GMAIL_COM;

    public static final HashMap<Integer, String> HOSTMAP = new HashMap<>();

    private boolean harConnected;

    private Properties properties;
    private Session session;
    private Store store;
    private String mailType;

    public Imap(){
        properties = setup();
        session = authenticate();
        store = store();
        mailType = IMAP_DEFAULT;
        harConnected = connect();
    }

    public Imap(int mailType) {
        leggTilalle();
        this.mailType = HOSTMAP.get(mailType);
        properties = setup();
        session = authenticate();
        store = store();
        harConnected = connect();
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

    public boolean connect() {
        try {
            if(store.isConnected()){
                return true;
            }else {
                store.connect(mailType, Attributter.FRAMAIL, Attributter.PASSORD);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return store.isConnected();
    }

    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Folder getInbox() {
        Folder inbox = null;
        if(!store.isConnected()){
            connect();
        }
        try {
            inbox = store.getFolder("Inbox");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return inbox;
    }

    public Folder getFolder(String mappe){
        if(mappe != null){
            try {
                if(!store.isConnected()){
                    connect();
                }
                return store.getFolder(mappe);
            }catch (MessagingException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public Folder[] getAllFolders(){
        try {
            if(!store.isConnected()){
                connect();
            }
            return store.getDefaultFolder().list("*");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkInbox() {
        if(!store.isConnected()){
            connect();
        }
        try {
            Folder inbox = store.getFolder("Inbox");
            System.out.println("Antall meldinger " + inbox.getMessageCount());
            System.out.println("Nye meldinger " + inbox.getUnreadMessageCount());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Folder opprettFolder(String navn){
        Folder nyfolder = null;
        if(!store.isConnected()){
            connect();
        }
        try {
            if(navn != null){
                nyfolder = store.getFolder(navn);
                nyfolder.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return nyfolder;
    }

    private void leggTilalle(){
            HOSTMAP.put(0, IMAP_GMAIL_COM);
            HOSTMAP.put(1, IMAP_OUTLOOK_COM);
            HOSTMAP.put(2, IMAP_YAHOO_COM);
    }

    public boolean isHarConnected() {
        return harConnected;
    }
}
