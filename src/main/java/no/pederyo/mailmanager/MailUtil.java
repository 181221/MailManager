package no.pederyo.mailmanager;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.*;

public class MailUtil {

    public Message[] hentAlleMailTilMappe(Folder mappe){
        Message[] messages = null;
        try {
            Folder folder = mappe;
            folder.open(Folder.READ_ONLY);
            messages = folder.getMessages();
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return messages;
    }

    public Message[] hentUlestMail(Folder folder){
        Message[] messages = null;
        try {
            folder.open(Folder.READ_ONLY);
            messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            folder.close(false);
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

    public void flyttMeldingerFraAvsenderTilMappe(Folder fraMappe, Folder tilMappe, String avsender){
        List<Message> meldinger = new ArrayList();
        try{
            Folder fra = fraMappe;
            Folder til = tilMappe;

            fra.open(Folder.READ_WRITE);
            til.open(Folder.READ_WRITE);
            Message[] messages = fra.search(EmailSearcher.soekAvsender(avsender));
            for(Message m : messages){
                meldinger.add(m);
                m.setFlag(Flags.Flag.DELETED, true);
            }
            Message[] tempMeldinger = meldinger.toArray(new Message[meldinger.size()]);
            fraMappe.copyMessages(tempMeldinger, tilMappe);
            fraMappe.expunge();
            fra.close(false);
            til.close(false);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void flyttMeldingerFraTil(Folder fraMappe, Folder tilMappe, Set<String> VALUES) {
        List<Message> meldinger = new ArrayList();
        try {
            fraMappe.open(Folder.READ_WRITE);
            tilMappe.open(Folder.READ_WRITE);
            for(Message m : fraMappe.getMessages()) {
                String subject = m.getSubject().toLowerCase();
                if(VALUES.contains(subject)) {
                    meldinger.add(m);
                    m.setFlag(Flags.Flag.DELETED, true);
                }
            }
            Message[] tempMeldinger = meldinger.toArray(new Message[meldinger.size()]);
            fraMappe.copyMessages(tempMeldinger, tilMappe);
            fraMappe.expunge();
            fraMappe.close(false);
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
        emailFolder.close(false);
    }
    

}
