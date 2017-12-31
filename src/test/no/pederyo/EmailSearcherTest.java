package no.pederyo;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import static org.junit.Assert.assertTrue;

/**
 * Created by Peder on 31.12.2017.
 */
public class EmailSearcherTest {
    private static final String EMAIL = "test@hvl.no", LOGIN = "hvl", PASSORD = "pass";

    private final String subject = GreenMailUtil.random();
    private final String body = GreenMailUtil.random();

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);
    ImapStub imap;
    @Before
    public void setup(){
        imap = new ImapStub(greenMail);
    }
    @Test
    public void testFinnerMailPaSubject() {
        SokeOrd sokeOrd = new SokeOrd();
        imap.sendMeld("Test", "test");
        imap.sendMeld("Kvittering", "test");
        imap.sendMeld("Ordre", "test");
        imap.sendMeld("ordre bekreftelse", "test");
        imap.sendMeld("Dette er en kvitering Kvittering", "test");
        imap.sendMeld("Denne meldingen er en Receipt", "test");
        Folder inbox = imap.hentFolder("INBOX");
        EmailSearcher es = new EmailSearcher(sokeOrd, inbox);
        Message[] msg = es.hentMeldingerFraSokeListe();
        assertTrue(msg.length == 5);

        sokeOrd.leggTilSokeOrd("VIKTIGMELDING");
        imap.sendMeld("VIKTIGMELDING", "test");
        msg = es.hentMeldingerFraSokeListe();
        assertTrue(msg.length == 6);

        sokeOrd.leggTilSokeOrd("VIkTIgMELDING");
        imap.sendMeld("VIkTIGMELDING", "test");
        msg = es.hentMeldingerFraSokeListe();
        assertTrue(msg.length == 7);
    }

}
