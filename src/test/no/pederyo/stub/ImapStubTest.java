package no.pederyo.stub;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.Folder;
import javax.mail.MessagingException;

import static org.junit.Assert.*;

public class ImapStubTest {
    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);

    private ImapStub imap;

    @Before
    public void setup(){
        imap = new ImapStub(greenMail);
    }

    @Test
    public void hentFolder() throws MessagingException {
        greenMail.start();
        imap.sendMeld("Heisann", "Test");
        Folder f = imap.getFolder("INBOX");
        Folder fake = imap.getFolder("fake");
        assertTrue(f.exists());
        assertFalse(fake.exists());
    }

    @Test
    public void getAllFolders() {
    }

    @Test
    public void sendMeld() {
    }

    @Test
    public void testOpprettMappe() throws MessagingException {
        imap.sendMeld("Test", "test");
        assertTrue(imap.getFolder("INBOX").exists());

        Folder nyfolder = imap.opprettFolder("Kvitteringer");
        Folder[] alleimap = imap.getStore().getDefaultFolder().list();

        assertTrue(alleimap.length == 2);
        assertTrue(nyfolder.exists());
    }
}