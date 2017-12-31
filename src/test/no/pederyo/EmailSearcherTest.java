package no.pederyo;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
import org.junit.Rule;

/**
 * Created by Peder on 31.12.2017.
 */
public class EmailSearcherTest {
    private static final String EMAIL = "test@hvl.no", LOGIN = "hvl", PASSORD = "pass";
    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);

    @Before
    public void setup(){
        ImapStub imap = new ImapStub(greenMail);
    }
}
