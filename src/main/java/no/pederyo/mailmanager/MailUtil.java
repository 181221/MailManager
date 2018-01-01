package no.pederyo.mailmanager;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class MailUtil {
    Folder folder;
    public Folder tilmappe;

    public MailUtil(Folder folder, Folder tilmappe){
        this.folder =folder;
        this.tilmappe = tilmappe;
    }

    public MailUtil(Folder folder){
        this.folder = folder;
    }

    public Message[] hentAlleMail(){
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

    public boolean slettAlleMail() throws MessagingException {
        return slett(folder.getMessages());
    }

    public boolean slettMail(Message[] messages) {
        return slett(messages);
    }

    public Message[] hentUlestMail() throws MessagingException {
        return folder.search(new FlagTerm(new Flags(Flag.SEEN), false));
    }

    public boolean flyttMeldinger(Folder tilMappe, Message[] messages){
        Boolean flyttet = false;
        if( kanFlyttes( folder, tilMappe, messages) ) {
            try {
                if( folder.exists() && tilMappe.exists() ) {

                    setFlagsDelete(messages);

                    folder.copyMessages(messages, tilMappe);

                    folder.expunge(); // Sletter mail med flag: DELETE.

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

    private boolean slett(Message[] messages){
        boolean slettet = false;
        if(messages != null || messages.length != 0){
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

    private void setFlagsDelete(Message[] messages) throws MessagingException {
        folder.setFlags(messages, new Flags(Flag.DELETED), true);
    }

    private void setFlagDelete(Message message) throws MessagingException {
       message.setFlag(Flag.DELETED, true);
    }

    private void lukk(Folder m, Folder m2) throws MessagingException {
        m.close(false);
        m2.close(false);
    }

    private boolean kanFlyttes(Folder fraMappe, Folder tilMappe, Message[] messages){
        return fraMappe != null && tilMappe != null && messages != null && messages.length > 0;
    }
    private boolean kanFlyttes(Folder fraMappe, Folder tilMappe, Message messages){
        return fraMappe != null && tilMappe != null && messages != null ;
    }



}
