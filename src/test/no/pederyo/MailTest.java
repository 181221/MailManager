package no.pederyo;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.stub.ImapStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.*;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by Peder on 30.12.2017.
 */
public class MailTest {

    private static final String EMAIL = "test@hvl.no", LOGIN = "hvl", PASSORD = "pass";

    private final String subject = GreenMailUtil.random();
    private final String body = GreenMailUtil.random();

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);

    private ImapStub imapStub;

    @Before
    public void setup(){
        imapStub = new ImapStub(greenMail);
    }


    @Test
    public void testSendOgRecive() throws UnsupportedEncodingException, MessagingException, UserException {
        greenMail.start();
        GreenMailUtil.sendTextEmailTest(LOGIN,
                EMAIL, "subject", "body"); //replace this with your send code
        assertEquals("body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

    @Test
    public void testRecive() throws MessagingException {
        greenMail.start();

        imapStub.sendMeld("Heisann", "funk pls:");
        Folder f = imapStub.getFolder("INBOX");
        assertTrue(f.getMessageCount() == 1);
        Message[] messages = greenMail.getReceivedMessages();
        assertNotNull(messages);
        assertEquals(messages[0].getSubject(),"Heisann");
        assertEquals(1, messages.length);
    }

    @Test
    public void testInboxMotarMail() throws MessagingException {
        greenMail.start();
        imapStub.sendMeld(subject, body);
        Folder f = imapStub.getFolder("INBOX");
        assertTrue(f.getMessageCount() == 1);
    }

}

