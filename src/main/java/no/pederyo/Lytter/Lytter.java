package no.pederyo.Lytter;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.util.Arrays;

public class Lytter implements Runnable {
    Folder folder;
    int freq;

    public Lytter(Folder folder, int freq) {
        this.folder = folder;
        this.freq = freq;
    }

    public void run() {
        try {
            leggTilLytter(folder, freq);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leggTilLytter(Folder folder, int freq) throws MessagingException, InterruptedException {
        folder.addMessageCountListener(new MessageCountAdapter() {
            public void messagesAdded(MessageCountEvent ev) {
                Message[] msgs = ev.getMessages();
                System.out.println("Got " + msgs.length + " new messages");
                // Just dump out the new messages
                for (int i = 0; i < msgs.length; i++) {
                    System.out.println("-----");
                    System.out.println("Message " + msgs[i].getMessageNumber() + ":");

                    try {
                        Address[] address = msgs[i].getFrom();
                        System.out.println("Tittel " + msgs[i].getSubject()
                                + " Fra " + Arrays.toString(address)
                                + " Sendt " + msgs[i].getSentDate());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        lyttHjelper(folder, 60000);
    }

    private void lyttHjelper(Folder folder, int freq) throws MessagingException, InterruptedException {
        boolean supportsIdle = false;
        try {
            if (folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
                supportsIdle = true;
            }
        } catch (FolderClosedException fex) {
            throw fex;
        } catch (MessagingException mex) {
            supportsIdle = false;
        }
        for (; ; ) {
            if (supportsIdle && folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
                System.out.println("IDLE done");
            } else {
                Thread.sleep(freq); // sleep for freq milliseconds

                // This is to force the IMAP server to send us
                // EXISTS notifications.
                folder.getMessageCount();
            }
        }
    }

}

