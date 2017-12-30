package no.pederyo.mailmanager;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.*;

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
            messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            folder.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public boolean flyttMeldinger(Folder fraMappe, Folder tilMappe, Message[] messages){
        List<Message> meldinger = new ArrayList();
        Boolean flyttet = false;
        if(kanFlyttes(fraMappe, tilMappe, messages)){
            try {
                if(fraMappe.exists() && tilMappe.exists()){
                    fraMappe.open(Folder.READ_WRITE);
                    tilMappe.open(Folder.READ_WRITE);

                    for(Message m : messages){
                        meldinger.add(m);
                        m.setFlag(Flags.Flag.DELETED, true);
                    }

                    Message[] tempMeldinger = meldinger.toArray(new Message[meldinger.size()]);
                    fraMappe.copyMessages(tempMeldinger, tilMappe);

                    fraMappe.expunge();

                    fraMappe.close(false);
                    tilMappe.close(false);
                    flyttet = true;
                }

            }catch (MessagingException e){
                e.printStackTrace();
            }
        }
        return flyttet;
    }

    private boolean kanFlyttes(Folder fraMappe, Folder tilMappe, Message[] messages){
        return fraMappe != null && tilMappe != null && messages != null && messages.length > 0;
    }

    public void visMail(Message[] messages){
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



}
