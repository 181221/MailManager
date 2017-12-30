package no.pederyo.mailmanager;

import no.pederyo.protokoll.IProtokoll;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.MessagingException;
public class Klient {
    public static void main(String[] args) throws MessagingException {
        IProtokoll imap = new Imap();
        imap.connect();

        MailUtil mailUtil = new MailUtil();

        imap.close();


    }
}
