package no.pederyo.Lytter;

import com.sun.mail.imap.IMAPFolder;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.util.Arrays;

public class Lytter implements Runnable {
    private Folder folder;
    private int freq;
    private EmailSearcher emailSearcher;
    private MailUtil mailUtil;

    public Lytter(Folder folder, int freq, EmailSearcher emailSearcher, MailUtil mailUtil) {
        this.folder = folder;
        this.freq = freq;
        this.emailSearcher = emailSearcher;
        this.mailUtil = mailUtil;
    }

    public void run() {
        try {
            leggTilLytter();
            lyttHjelper();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void leggTilLytter() {
        folder.addMessageCountListener(new MessageCountAdapter() {
            public void messagesAdded(MessageCountEvent ev) {
                Message[] msgs = ev.getMessages();
                System.out.println("Got " + msgs.length + " new messages");
                // Just dump out the new messages
                for (int i = 0; i < msgs.length; i++) {
                    Message msg = msgs[i];
                    System.out.println("-----");
                    System.out.println("Message " + msg.getMessageNumber() + ":");
                    try {
                        Address[] address = msgs[i].getFrom();
                        String subject = msgs[i].getSubject();
                        System.out.println("Tittel " + subject
                                + " Fra " + Arrays.toString(address)
                                + " Sendt " + msgs[i].getSentDate());
                        if(emailSearcher.subjectExists(subject)){
                            mailUtil.flyttMeldinger(mailUtil.tilmappe, new Message[]{msg});
                            if(mailUtil.tilmappe.isOpen()){
                                mailUtil.tilmappe.close(false);
                                mailUtil.tilmappe.open(Folder.READ_ONLY);;
                            }
                            System.out.println("Mailen inneholder sokeord..");
                            System.out.println("Flytter melding til " + mailUtil.tilmappe.getName());
                        }

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void lyttHjelper() throws MessagingException, InterruptedException {
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

