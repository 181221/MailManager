package no.pederyo.stub;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import no.pederyo.protokoll.IConnect;

import javax.mail.*;

/**
 * Created by Peder on 31.12.2017.
 */
public class ImapStub implements IConnect {

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

    public Folder hentFolder(String type){
        Folder folder = null;
        try {
            folder = store.getFolder(type);
            folder.open(1);
            Message[] msg = folder.getMessages();
            folder.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return folder;
    }

    public void sendMeld(String subject, String melding) {
        GreenMailUtil.sendTextEmail(EMAIL, EMAIL, subject,
                melding,
                greenMail.getSmtp().getServerSetup());
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

    public Store getStore() {
        return store;
    }
}
