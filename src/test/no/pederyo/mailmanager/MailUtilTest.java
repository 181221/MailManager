package no.pederyo.mailmanager;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import no.pederyo.stub.ImapStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    public void testHentAlleMail(){
        //TODO
    }

    @Test
    public void testHentUlestMail(){
        //TODO
    }

    @Test
    public void testFlyttMeldinger(){
        //TODO
    }
}
