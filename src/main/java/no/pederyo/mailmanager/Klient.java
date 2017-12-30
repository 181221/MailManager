package no.pederyo.mailmanager;

import no.pederyo.Attributter;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
public class Klient {
    public static void main(String[] args) throws MessagingException {
        Imap imap = new Imap();
        imap.connect();
        Folder inbox = imap.getMappe("Inbox");

        SokeOrd sokeOrd = new SokeOrd(Attributter.SOKEORD);

        MailUtil mailUtil = new MailUtil();

        Message[] m = mailUtil.hentAlleMailTilMappe(inbox);
        mailUtil.visMail(m);

        imap.close();

    }
}
