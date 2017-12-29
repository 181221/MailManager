package no.pederyo;

import no.pederyo.mailmanager.MailUtil;
import no.pederyo.protokoll.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
public class Klient {
    public static void main(String[] args) throws MessagingException {
        MailUtil mailUtil = new MailUtil();
        Imap imap = new Imap();

        //Folder inbox = mailUtil.getMappe("INBOX");
        //mailUtil.flyttMeldingerFraAvsenderTilMappe(inbox,inbox,"noreply@plex.tv");
        //MailUtil.status(imap);
        //MailUtil.lytter(imap);
        //MailUtil.organiserInbox(imap, "Kvitteringer");
        //MailUtil.checkInbox(imap);
        //MailUtil.check(imap);

    }
}
