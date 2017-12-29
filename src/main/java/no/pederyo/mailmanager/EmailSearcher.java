package no.pederyo.mailmanager;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import java.util.Arrays;
import java.util.HashSet;

public class EmailSearcher {

    public static HashSet<String> opprettSoekeSett(){
       return new HashSet<String>();
    }

    public static boolean leggTilSokeOrd(HashSet<String> hashSet, String ord){
        return hashSet.add(ord);
    }

    public static boolean leggTilSokeOrd(HashSet<String> hashSet, String[] ord){
        String[] cpord = new String[ord.length];
        for(int i = 0; i < cpord.length; i++){
            String meld = ord[i];
            if(meld != null){
                meld.toLowerCase();
                cpord[i] = meld;
            }
        }
        return hashSet.addAll(Arrays.asList(cpord));
    }

    public static SearchTerm soekAvsender(final String fraEmail){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject().contains(fraEmail)) {
                        return true;
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
