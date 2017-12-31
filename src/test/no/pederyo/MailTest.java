package no.pederyo;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
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

    ImapStub imapStub;

    @Before
    public void setup(){
        imapStub = new ImapStub(greenMail);
    }


    @Test
    public void testSendOgRecive() throws UnsupportedEncodingException, MessagingException, UserException {
        GreenMailUtil.sendTextEmailTest(LOGIN,
                EMAIL, "subject", "body"); //replace this with your send code
        assertEquals("body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

    }

    @Test
    public void testRecive() throws MessagingException {
        greenMail.start();
        sendTestTwoMails();
        assertTrue(greenMail.waitForIncomingEmail(5000, 2));
        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(2, messages.length);
    }

    @Test
    public void testInboxMotarMail() throws MessagingException {
        greenMail.start();
        sendTestTwoMails();
        Folder f = imapStub.hentFolder("INBOX");
        assertTrue(f.getMessageCount() == 2);
    }

    private void sendTestTwoMails() throws MessagingException {
        GreenMailUtil.sendTextEmailTest(EMAIL, EMAIL, subject, body);

        // Create multipart
        MimeMultipart multipart = new MimeMultipart();
        final MimeBodyPart part1 = new MimeBodyPart();
        part1.setContent("body1", "text/plain");
        multipart.addBodyPart(part1);
        final MimeBodyPart part2 = new MimeBodyPart();
        part2.setContent("body2", "text/plain");
        multipart.addBodyPart(part2);

        GreenMailUtil.sendMessageBody(EMAIL, EMAIL, subject + "__2", multipart, null, ServerSetupTest.SMTP);
    }
}

