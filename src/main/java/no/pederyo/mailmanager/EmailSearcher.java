package no.pederyo.mailmanager;

import no.pederyo.protokoll.IImap;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

public class EmailSearcher {
    private IImap imap;
    volatile SokeOrd sokeOrd;
    private Folder folder;
    private String beskrivelse;

    public EmailSearcher(SokeOrd sokeOrd, Folder folder, IImap imap) {
        this.sokeOrd = sokeOrd;
        this.imap =  imap;
        this.folder = folder;
    }

    public Message[] hentMeldingerTilAvsender(String avsender){
        Message[] meldinger = null;
        try {
            Folder f = imap.getFolder(folder.getName());
            if(!f.isOpen())
                f.open(Folder.READ_WRITE);

            meldinger = f.search(soekAvsender(avsender));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return meldinger;
    }

    public Message[] hentMeldingerFraSokeListe(){
        Message[] meldinger = null;
        try {
            Folder f = imap.getFolder(folder.getName());
            if(!f.isOpen())
                f.open(Folder.READ_WRITE);

            meldinger = f.search(soekMailMedSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return meldinger;
    }

    private SearchTerm soekAvsender(final String fraEmail){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    Address[] fromAddress = message.getFrom();
                    if (fromAddress != null && fromAddress.length > 0) {
                        if (fromAddress[0].toString().contains(fraEmail)) {
                            return true;
                        }
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };
        return searchCondition;
    }

    private SearchTerm soekMailMedSubject(){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    String subject = message.getSubject().toLowerCase();
                    if(subjectExists(subject))
                        return true;
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };
        return searchCondition;
    }

    public boolean subjectExists(String tittel){
        String subject = tittel.toLowerCase();
        if (subject != null) {
            String[] sub = subject.split(" ");
            for(int i = 0; i < sub.length; i++) {
                if (sokeOrd.getOrdliste().contains(sub[i])) {
                    return true;
                }
            }
        }
        return false;

    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public SokeOrd getSokeOrd() {
        return sokeOrd;
    }

    public Folder getFolder() {
        return imap.getFolder(folder.getName());
    }
}
