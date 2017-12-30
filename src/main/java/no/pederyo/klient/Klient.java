package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class Klient {

    public static void main(String[] args) throws MessagingException {

            Imap imap = new Imap();

            imap.connect();

            SokeOrd sokeOrd = new SokeOrd();

            Folder inbox = imap.getFolder("INBOX");

            EmailSearcher es = new EmailSearcher(sokeOrd, inbox);

            es.hentMeldingerFraSokeListe();

            MailUtil mailUtil = new MailUtil(inbox);

            mailUtil.printUt(es.hentMeldingerFraSokeListe());

    }
}
