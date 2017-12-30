package no.pederyo.mailmanager;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class MailUtil {
    Folder folder;

    public MailUtil(Folder folder){
        this.folder = folder;
    }

    public Message[] hentAlleMail(){
        Message[] messages = null;
        try {
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

    public Message[] hentUlestMail(){
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

    public boolean flyttMeldinger(Folder tilMappe, Message[] messages){
        Boolean flyttet = false;
        if( kanFlyttes( folder, tilMappe, messages) ) {
            try {
                if( folder.exists() && tilMappe.exists() ) {

                    SetFlags(messages);

                    folder.copyMessages(messages, tilMappe);

                    folder.expunge(); // Sletter mail med flag: DELETE.

                    lukk(folder, tilMappe);

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

    private void SetFlags(Message[] messages) throws MessagingException {
        folder.open(Folder.READ_WRITE);
        folder.setFlags(messages, new Flags(Flag.DELETED), true);
    }

    private void lukk(Folder m, Folder m2) throws MessagingException {
        m.close(false);
        m2.close(false);
    }

    private boolean kanFlyttes(Folder fraMappe, Folder tilMappe, Message[] messages){
        return fraMappe != null && tilMappe != null && messages != null && messages.length > 0;
    }



}
