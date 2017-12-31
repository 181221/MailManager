package no.pederyo.klient;

import no.pederyo.Lytter.Lytter;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.Arrays;

public class Klient {

    public static void main(String[] args) throws MessagingException {

            Imap imap = new Imap();

            SokeOrd sokeOrd = new SokeOrd();

            Folder inbox = imap.getFolder("INBOX");

            Folder Kvitteringer = imap.getFolder("Kvitteringer");

            inbox.open(Folder.READ_WRITE);

            EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, inbox);

            MailUtil mailUtil = new MailUtil(inbox, Kvitteringer);

            Lytter lytter = new Lytter(inbox, 60000, emailSearcher, mailUtil);

            Thread thread = new Thread(lytter);

            thread.start();

            emailSearcher.hentMeldingerFraSokeListe();


            mailUtil.printUt(emailSearcher.hentMeldingerFraSokeListe());

    }
}
