package no.pederyo.mailmanager;

import com.sun.mail.imap.IMAPFolder;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.util.MailUtil;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.util.Arrays;

public class Lytter implements Runnable {
    private Folder fra;
    private Folder til;
    private int freq;
    private EmailSearcher emailSearcher;

    public Lytter(Folder fra, Folder til, int freq, EmailSearcher emailSearcher) {
        this.fra = fra;
        this.til = til;
        this.freq = freq;
        this.emailSearcher = emailSearcher;
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
        fra.addMessageCountListener(new MessageCountAdapter() {
            public void messagesAdded(MessageCountEvent ev) {
                Message[] msgs = ev.getMessages();
                System.out.println("Got " + msgs.length + " new messages");
                for (int i = 0; i < msgs.length; i++) {
                    Message msg = msgs[i];
                    try {
                        skrivUtMeld(msg);
                        if(emailSearcher.subjectExists(msg.getSubject())){
                            flyttMeld(msg);
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void skrivUtMeld(Message msg) throws MessagingException {
        System.out.println("-----");
        System.out.println("Message " + msg.getMessageNumber() + ":");
        Address[] address = msg.getFrom();
        String subject = msg.getSubject();
        System.out.println("Tittel " + subject
                + " Fra " + Arrays.toString(address)
                + " Sendt " + msg.getSentDate());
    }

    private void flyttMeld(Message msg) throws MessagingException {
        System.out.println("Mailen inneholder sokeord.. fra listen til " + emailSearcher.getBeskrivelse());
        System.out.println("Flytter mailen til mappen " + til.getName());
        MailUtil.flyttMeldinger(fra, til, new Message[]{msg});
        if(til.isOpen()){
            til.close(false);
            til.open(Folder.READ_ONLY);
        }
    }

    private void lyttHjelper() throws MessagingException, InterruptedException {
        boolean supportsIdle = false;
        try {
            if (fra instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) fra;
                f.idle();
                supportsIdle = true;
            }
        } catch (FolderClosedException fex) {
            throw fex;
        } catch (MessagingException mex) {
            supportsIdle = false;
        }
        while (true) {
            if (supportsIdle && fra instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) fra;
                f.idle();
            } else {
                Thread.sleep(freq);
                fra.getMessageCount();
            }
        }
    }

    public Folder getFra() {
        return fra;
    }

    public Folder getTil() {
        return til;
    }

    public int getFreq() {
        return freq;
    }

    public EmailSearcher getEmailSearcher() {
        return emailSearcher;
    }

}

