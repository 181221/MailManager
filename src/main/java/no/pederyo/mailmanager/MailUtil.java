package no.pederyo.mailmanager;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class MailUtil {

    public Message[] hentAlleMail(Folder mappe){
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
            messages = folder.search(new FlagTerm(new Flags(Flag.SEEN), false));
            folder.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public boolean flyttMeldinger(Folder fraMappe, Folder tilMappe, Message[] messages){
        Boolean flyttet = false;
        if( kanFlyttes(fraMappe, tilMappe, messages) ) {
            try {
                if( fraMappe.exists() && tilMappe.exists() ) {

                    SetFlags(fraMappe, messages);

                    fraMappe.copyMessages(messages, tilMappe);

                    fraMappe.expunge(); // Sletter mail med flag: DELETE.

                    lukk(fraMappe, tilMappe);

                    flyttet = true;
                }
            }catch ( MessagingException e ){
                e.printStackTrace();
            }
        }
        return flyttet;
    }

    public void printUt(Message[] messages){
        if(messages.length > 0){
            Folder folder = messages[0].getFolder();
            if(!folder.isOpen()){
                try {
                    folder.open(Folder.READ_ONLY);
                    for ( Message message : messages ) {
                            System.out.println("sendDate: " + message.getSentDate() +
                                    " subject: " + message.getSubject() );
                    }
                    folder.close();
                }catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // -------------------- HJELPE METODER --------------------

    private void SetFlags(Folder fraMappe, Message[] messages) throws MessagingException {
        fraMappe.open(Folder.READ_WRITE);
        fraMappe.setFlags(messages, new Flags(Flag.DELETED), true);
    }

    private void lukk(Folder m, Folder m2) throws MessagingException {
        m.close(false);
        m2.close(false);
    }

    private boolean kanFlyttes(Folder fraMappe, Folder tilMappe, Message[] messages){
        return fraMappe != null && tilMappe != null && messages != null && messages.length > 0;
    }



}
