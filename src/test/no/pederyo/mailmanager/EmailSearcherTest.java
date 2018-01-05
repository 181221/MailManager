package no.pederyo.mailmanager;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.modell.SokeOrd;
import no.pederyo.protokoll.IImap;
import no.pederyo.stub.ImapStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import static no.pederyo.stub.ImapStub.EMAIL;
import static org.junit.Assert.assertTrue;

/**
 * Created by Peder on 31.12.2017.
 */
public class EmailSearcherTest {
    private final String subject = GreenMailUtil.random();
    private final String body = GreenMailUtil.random();

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);

    private IImap imap;

    @Before
    public void setup(){
        imap = new ImapStub(greenMail);
    }
    public void sendMeld(String subject, String melding) {
        GreenMailUtil.sendTextEmail(EMAIL, EMAIL, subject,
                melding,
                greenMail.getSmtp().getServerSetup());
    }
    @Test
    public void testOppdatertMappe() throws MessagingException {
        SokeOrd sokeOrd = new SokeOrd();
        Folder inbox = imap.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        EmailSearcher es = new EmailSearcher(sokeOrd, inbox, imap);

        sendMeld("Test", "test");
        assertTrue(es.getFolder().getMessageCount() == 1);

        sendMeld("Test", "test");
        assertTrue(es.getFolder().getMessageCount() == 2);

        sendMeld("Test", "test");
        assertTrue(es.getFolder().getMessageCount() == 3);
    }
    @Test
    public void testfinnerMailpaaAvsender() throws MessagingException {
        greenMail.start();
        SokeOrd sokeOrd = new SokeOrd();
        sendMeld("Loool", "test");
        Folder inbox = imap.getInbox();
        inbox.open(2);

        EmailSearcher es = new EmailSearcher(sokeOrd, inbox, imap);
        GreenMailUtil.sendTextEmail(EMAIL, "olav@gunnar.com", subject,
                "lol",
                greenMail.getSmtp().getServerSetup());
        assertTrue(imap.getInbox().getMessageCount() == 2);

        assertTrue(es.hentMeldingerTilAvsender("olav@gunnar.com").length == 1);

        GreenMailUtil.sendTextEmail(EMAIL, "olav@gunnar.com", subject,
                "lol",
                greenMail.getSmtp().getServerSetup());

        assertTrue(es.hentMeldingerTilAvsender("olav@gunnar.com").length == 2);

        sendMeld("VIKTIGMELDING", "test");

        assertTrue(es.hentMeldingerTilAvsender(EMAIL).length == 2);

    }

    @Test
    public void testFinnerMailPaSubject() throws MessagingException {
        SokeOrd sokeOrd = new SokeOrd();
        sendMeld("Test", "test");
        sendMeld("Kvittering", "test");
        sendMeld("Ordre", "test");
        sendMeld("ordre bekreftelse", "test");
        sendMeld("Dette er en kvitering Kvittering", "test");
        sendMeld("Denne meldingen er en Receipt", "test");
        Folder inbox = imap.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        EmailSearcher es = new EmailSearcher(sokeOrd, inbox, imap);
        Message[] msg = es.hentMeldingerFraSokeListe();
        assertTrue(msg.length == 5);

        es.getSokeOrd().leggTilSokeOrd("VIKTIGMELDING");

        sendMeld("VIKTIGMELDING", "test");

        msg = es.hentMeldingerFraSokeListe();

        assertTrue(msg.length == 6);

        es.getSokeOrd().leggTilSokeOrd("VIKTIGMELDING");
        sendMeld("VIkTIGMELDING", "test");
        msg = es.hentMeldingerFraSokeListe();
        assertTrue(msg.length == 7);
    }



    @Test
    public void testHentMeldingerTilAvsender(){
        //todo
    }



}
