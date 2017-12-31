package no.pederyo.klient;

import no.pederyo.Lytter.Lytter;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class Klient {

    public static void main(String[] args) throws MessagingException {

            Imap imap = new Imap();

            imap.connect();

            SokeOrd sokeOrd = new SokeOrd();

            Folder inbox = imap.getFolder("INBOX");

            inbox.open(Folder.READ_WRITE);

            EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, inbox);

            Lytter lytter = new Lytter(inbox, 60000, emailSearcher);

            Thread thread = new Thread(lytter);

            thread.start();

            emailSearcher.hentMeldingerFraSokeListe();

            MailUtil mailUtil = new MailUtil(inbox);

            mailUtil.printUt(emailSearcher.hentMeldingerFraSokeListe());

    }
}
