package no.pederyo.mailmanager;

import no.pederyo.Attributter;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
public class Klient {
    public static void main(String[] args) throws MessagingException {
        Imap imap = new Imap();
        imap.connect();
        Flags Flags = new Flags(javax.mail.Flags.Flag.DELETED);
        Flags.getSystemFlags();
        Folder inbox = imap.getMappe("Inbox");
        MailUtil mailUtil = new MailUtil();

        //Folder kvitteringer = imap.getMappe("Kvitteringer");
        SokeOrd sokeOrd = new SokeOrd(Attributter.SOKEORD);

        mailUtil.printUt(EmailSearcher.hentMeldingerFraSokeListe(inbox, sokeOrd.getOrdliste()));
        //mailUtil.flyttMeldinger(inbox, kvitteringer, meldinger);

        imap.close();

    }
}
