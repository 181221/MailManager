package no.pederyo.mailmanager;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import java.util.HashSet;
import java.util.Set;

public class EmailSearcher {

    public static Message[] hentMeldingerTilAvsender(Folder folder, String avsender){
        Message[] meldinger = null;
        try {
            folder.open(Folder.READ_ONLY);
            meldinger = folder.search(soekAvsender(avsender));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return meldinger;
    }

    public static Message[] hentMeldingerFraSokeListe(Folder folder, HashSet<String> ordliste){
        Message[] meldinger = null;
        try {
            folder.open(Folder.READ_ONLY);
            meldinger = folder.search(soekEtterMeldinger(ordliste));
            folder.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return meldinger;
    }

    private static SearchTerm soekAvsender(final String fraEmail){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    Address[] fromAddress = message.getFrom();
                    System.out.println(message.getAllRecipients()[0].toString());
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

    private static SearchTerm soekEtterMeldinger(final Set<String> ordliste){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    String subject = message.getSubject().toLowerCase();
                    if (subject != null) {
                        String[] sub = subject.split(" ");
                        for(int i = 0; i < sub.length; i++) {
                            if (ordliste.contains(sub[i])) {
                                return true;
                            }
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
}
