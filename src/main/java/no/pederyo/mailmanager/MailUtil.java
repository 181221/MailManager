package no.pederyo.mailmanager;

import no.pederyo.Attributter;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.Imap;
import no.pederyo.protokoll.Pop3;
import no.pederyo.protokoll.Smtp;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.*;

public class MailUtil {
    private static final Set<String> VALUES = new HashSet<String>(Arrays.asList(
            new String[] {"kvittering","ordre","ordrebekreftelse","bekreftelse","receipt", "reisedokumenter"}
    ));
    private Imap imap;
    public MailUtil(){
        imap = new Imap();
    }

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
    public Message[] hentAlleMailTilMappe(Folder mappe){
        Message[] messages = null;
        try {
            imap.connect();
            Store store = imap.getStore();
            Folder folder = mappe;
            folder.open(Folder.READ_ONLY);
            messages = folder.getMessages();
            imap.close();
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return messages;
    }

    public Message[] hentUlestMail(){
        Message[] messages = null;
        try {
            imap.connect();
            Store store = imap.getStore();
            Folder inbox = store.getDefaultFolder().list()[0];
            inbox.open(Folder.READ_ONLY);
            messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            imap.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }
    public static void visUlestMail(Message[] messages){
        for ( Message message : messages ) {
            try {
                System.out.println(
                        "sendDate: " + message.getSentDate()
                                + " subject:" + message.getSubject() );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
    public static Folder hentMappe(String mappe){
        return null;
    }
    public static void flyttMeldingerFraMappe(Folder fraMappe, Folder tilmappe, String avsender){

    }

    public void organiserInbox(Folder tilMappe) {
        List<Message> meldinger = new ArrayList();
        try {
            imap.connect();
            Store store = imap.getStore();
            Folder inbox = store.getDefaultFolder().list()[0];
            Folder mappen = tilMappe;
            inbox.open(Folder.READ_WRITE);
            mappen.open(Folder.READ_WRITE);
            for(Message m : inbox.getMessages()) {
                String subject = m.getSubject().toLowerCase();
                if(VALUES.contains(subject)) {
                    meldinger.add(m);
                    m.setFlag(Flags.Flag.DELETED, true);
                }
            }
            Message[] tempMeldinger = meldinger.toArray(new Message[meldinger.size()]);
            inbox.copyMessages(tempMeldinger, mappen);
            inbox.expunge();
            imap.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void lytter() {
        try {
            imap.connect();
            Store store = imap.getStore();
            final Folder inbox = store.getFolder("INBOX");
            final MessageCountAdapter listener = new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();
                    for (Message message : messages) {
                        try {
                            System.out.println("Mail Subject:- " + message.getSubject());
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            inbox.addMessageCountListener(listener);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void checkInbox() {
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

    private static void printUtMeldingerTilMappe(Folder emailFolder) throws MessagingException, IOException {
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
