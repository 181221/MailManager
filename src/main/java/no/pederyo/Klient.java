package no.pederyo;

import no.pederyo.mailmanager.MailUtil;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.Imap;
import no.pederyo.protokoll.Smtp;

import javax.mail.MessagingException;
import javax.mail.Store;

public class Klient {
    public static void main(String[] args) throws MessagingException {
        Imap imap = new Imap();
        MailUtil.checkInbox(imap);
        //MailUtil.check(imap);

    }


    public static void sendMail() {
        System.out.println("Connecting to service");
        Mail mail = new Mail("hvl.noreply@gmail.com", "hvl.noreply@gmail.com", "Hei","Test");
        Smtp smtp = new Smtp();
        System.out.println("Sending mail");
        MailUtil.send(mail, smtp);
        System.out.println(mail.getResult());
    }
}
