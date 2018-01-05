package no.pederyo.util;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class MailUtil {

    public static Message[] hentAlleMail(Folder folder){
        Message[] messages = null;
        try {
            messages = folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return messages;
    }

    public static boolean slettAlleMail(Folder folder) throws MessagingException {
        return slett(folder.getMessages());
    }

    public static boolean slettMail(Message[] messages) {
        return slett(messages);
    }

    public static Message[] hentUlestMail(Folder folder) throws MessagingException {
        return folder.search(new FlagTerm(new Flags(Flag.SEEN), false));
    }

    public static boolean flyttMeldinger(Folder fra, Folder til, Message[] messages){
        Boolean flyttet = false;
        if( kanFlyttes(fra, til, messages) ) {
            try {
                if( fra.exists() && til.exists() ) {

                    fra.copyMessages(messages, til);

                    setFlagsDelete(messages);

                    fra.expunge(); // Sletter mail med flag: DELETE.

                    flyttet = true;
                }
            }catch ( MessagingException e ){
                e.printStackTrace();
            }
        }
        return flyttet;
    }

    public static void printUt(Message[] messages){
        if(messages.length > 0){
            Folder folder = messages[0].getFolder();
            if(!folder.isOpen()){
                try {
                    folder.open(Folder.READ_ONLY);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            for ( Message message : messages ) {
                try {
                    System.out.println("sendDate: " + message.getSentDate() +
                            " subject: " + message.getSubject() );
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // -------------------- HJELPE METODER --------------------

    private static boolean slett(Message[] messages){
        boolean slettet = false;
        if(messages != null || messages.length != 0){
            Folder folder = messages[0].getFolder();
            int antall = messages.length;
            try {
                setFlagsDelete(messages);
                folder.expunge();
                slettet = antall != folder.getMessageCount();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return slettet;
    }

    private static void  setFlagsDelete(Message[] messages) throws MessagingException {
        messages[0].getFolder().setFlags(messages, new Flags(Flag.DELETED), true);
    }

    private static void setFlagDelete(Message message) throws MessagingException {
       message.setFlag(Flag.DELETED, true);
    }

    private void lukk(Folder m, Folder m2) throws MessagingException {
        m.close(false);
        m2.close(false);
    }

    private static boolean kanFlyttes(Folder fra, Folder til, Message[] messages){
        return fra != null && til != null && messages != null && messages.length > 0;
    }
    private boolean kanFlyttes(Folder fra, Folder til, Message messages){
        return fra != null && til != null && messages != null ;
    }
}
