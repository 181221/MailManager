package no.pederyo.mailmanager;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.stub.ImapStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Peder on 31.12.2017.
 */
public class MailUtilTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);

    private ImapStub imap;

    @Before
    public void setup(){
        imap = new ImapStub(greenMail);
    }

    @Test
    public void testHentAlleMail() throws MessagingException {
        greenMail.start();
        sendMeldinger(10);
        Folder f = imap.getFolder("INBOX");
        f.open(Folder.READ_ONLY);
        MailUtil mu = new MailUtil(f);
        assertTrue(f.isOpen());
        Message[] msg = mu.hentAlleMail();
        assertFalse(msg == null);
        assertFalse(msg.length == 1);
        assertTrue(msg.length == 10);
    }

    @Test
    public void testHentUlestMail() throws MessagingException {
        greenMail.start();
        sendMeldinger(10);
        Folder f = imap.getFolder("INBOX");
        f.open(Folder.READ_ONLY);
        MailUtil mu = new MailUtil(f);

        Message[] msg = mu.hentAlleMail();
        assertTrue(mu.hentUlestMail() != null);
        assertFalse(mu.hentUlestMail().length == 3);
        assertTrue(mu.hentUlestMail().length == msg.length);
        sendMeldinger(5);
    }
    @Test
    public void testSlett() throws MessagingException {
        MailUtil mu = startogconnect();
        Message[] msg = mu.hentAlleMail();
        assertTrue(mu.slettMail(msg));
    }


    @Test
    public void testSlettAlleMail() throws MessagingException {
        MailUtil mu = startogconnect();
        boolean slettet = mu.slettAlleMail();
        assertTrue(slettet);
    }

    @Test
    public void testFlyttMeldinger() throws MessagingException {
        MailUtil mu = startogconnect();
        Folder kvittering = imap.opprettFolder("Kvittering");
        Folder inbox = mu.folder;
        kvittering.open(Folder.READ_WRITE);
        Message[] meldinger = inbox.getMessages();
        assertTrue(mu.flyttMeldinger(kvittering, meldinger));
        inbox = imap.getFolder("INBOX");
        assertTrue(inbox.getMessageCount() == 0);

    }

    private void sendMeldinger(int antall){
        for(int i = 0; i < antall; i++){
            imap.sendMeld("Heisann", "Test");
        }
    }

    private MailUtil startogconnect(){
        greenMail.start();
        sendMeldinger(10);
        Folder f = imap.getFolder("INBOX");
        try {
            f.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new MailUtil(f);
    }
}
