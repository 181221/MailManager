package no.pederyo.mailmanager;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.stub.ImapStub;
import no.pederyo.util.MailUtil;
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
        assertTrue(f.isOpen());
        Message[] msg = MailUtil.hentAlleMail(f);
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

        Message[] msg = MailUtil.hentAlleMail(f);
        assertTrue(MailUtil.hentUlestMail(f) != null);
        assertFalse(MailUtil.hentUlestMail(f).length == 3);
        assertTrue(MailUtil.hentUlestMail(f).length == msg.length);
        sendMeldinger(5);
    }
    @Test
    public void testSlett() throws MessagingException {
        Folder f = startogconnect();
        Message[] msg = MailUtil.hentAlleMail(f);
        assertTrue(MailUtil.slettMail(msg));
    }


    @Test
    public void testSlettAlleMail() throws MessagingException {
        Folder f = startogconnect();
        boolean slettet = MailUtil.slettAlleMail(f);
        assertTrue(slettet);
    }

    @Test
    public void testFlyttMeldinger() throws MessagingException {
        Folder f = startogconnect();
        Folder kvittering = imap.opprettFolder("Kvittering");
        kvittering.open(Folder.READ_WRITE);
        Message[] meldinger = f.getMessages();
        assertTrue(MailUtil.flyttMeldinger(f, kvittering, meldinger));
        f = imap.getFolder("INBOX");
        assertTrue(f.getMessageCount() == 0);

    }

    private void sendMeldinger(int antall){
        for(int i = 0; i < antall; i++){
            imap.sendMeld("Heisann", "Test");
        }
    }

    private Folder startogconnect(){
        greenMail.start();
        sendMeldinger(10);
        Folder f = imap.getFolder("INBOX");
        try {
            f.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return f;
    }
}
