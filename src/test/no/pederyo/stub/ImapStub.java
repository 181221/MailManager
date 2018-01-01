package no.pederyo.stub;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import no.pederyo.protokoll.IConnect;
import no.pederyo.protokoll.IImap;

import javax.mail.*;

/**
 * Created by Peder on 31.12.2017.
 */
public class ImapStub implements IConnect, IImap {

    public static final String EMAIL = "test@hvl.no", LOGIN = "hvl", PASSORD = "pass";

    private Store store;

    private GreenMailRule greenMail;

    public ImapStub(GreenMailRule greenMail){
        this.greenMail = greenMail;
        greenMail.setUser(EMAIL, LOGIN, PASSORD);
        store = store();
        connect();
    }

    public Store store() {
        try {
            return greenMail.getImap().createStore();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connect() {
        try {
            store.connect(LOGIN, PASSORD);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
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

    public Folder getFolder(String type){
        if(type != null){
            try {
                if(!store.isConnected()){
                    connect();
                }
                return store.getFolder(type);
            }catch (MessagingException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Folder getInbox() {
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


    public void sendMeld(String subject, String melding) {
        GreenMailUtil.sendTextEmail(EMAIL, EMAIL, subject,
                melding,
                greenMail.getSmtp().getServerSetup());
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
        }
        return nyfolder;
    }

    public Store getStore() {
        return store;
    }
}
