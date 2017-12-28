package no.pederyo.mailmanager;

import no.pederyo.Attributter;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.Imap;
import no.pederyo.protokoll.Pop3;
import no.pederyo.protokoll.Smtp;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MailUtil {

    public static String send(Mail mail, Smtp smtp) {
        try {
            MimeMessage message = new MimeMessage(smtp.getSession());
            message.setFrom(new InternetAddress(mail.getFra()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTil()));
            message.setSubject(mail.getSubject());
            message.setText(mail.getMsg());
            javax.mail.Transport.send(message);
            mail.setResult("Your mail sent successfully....");
        } catch (AddressException e) {
            mail.setResult(e.getMessage());
        } catch (javax.mail.MessagingException e) {
            mail.setResult(e.getMessage());
        }
        return mail.getResult();
    }
    //byttes ut med regex etterhvert.
    public static boolean subjectInnholderOrd(String subject){
        return subject.contains("kvittering") || subject.contains("ordre") || subject.contains("ordrebekreftelse") || subject.contains("bekreftelse");
    }

    public static void organiserInbox(Imap imap, String tilMappe) {
        List<Message> meldinger = new ArrayList();
        try {
            imap.connect();
            Store store = imap.getStore();
            Folder inbox = store.getDefaultFolder().list()[0];
            Folder mappen = store.getFolder(tilMappe);
            inbox.open(Folder.READ_WRITE);
            mappen.open(Folder.READ_WRITE);

            for(Message m : inbox.getMessages()) {
                String subject = m.getSubject().toLowerCase();
                String melding = m.getContent().toString();
                if(subjectInnholderOrd(subject))
                    meldinger.add(m);
                else {
                    if(subjectInnholderOrd(melding))
                        meldinger.add(m);
                }
            }
            Message[] tempMeldinger = meldinger.toArray(new Message[meldinger.size()]);
            inbox.copyMessages(tempMeldinger, mappen);
            imap.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void checkInbox(Imap imap) {
        try {
            imap.connect();
            Store store = imap.getStore();
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            System.out.println("Antall meldinger " + inbox.getMessageCount());
            System.out.println("Nye meldinger " + inbox.getUnreadMessageCount());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void printUtAlleMeldinger(Pop3 pop3, String type){
        try {
            Store store = pop3.getStore();
            store.connect(Attributter.POP3HOST, Attributter.FRAMAIL, Attributter.PASSORD);
            Folder emailFolder = store.getFolder("Inbox");
            printUtMeldinger(emailFolder);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUtMeldinger(Folder emailFolder) throws MessagingException, IOException {
        emailFolder.open(Folder.READ_ONLY);
        emailFolder.getFullName();
        Message[] messages = emailFolder.getMessages();
        System.out.println("messages.length---" + messages.length);

        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            System.out.println("---------------------------------");
            System.out.println("Email Number " + (i + 1));
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + message.getContent().toString());

        }
    }

}
