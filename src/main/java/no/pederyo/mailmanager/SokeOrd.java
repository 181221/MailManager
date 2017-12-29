package no.pederyo.mailmanager;

import java.util.Arrays;
import java.util.HashSet;

public class SokeOrd {
    private HashSet<String> ordliste;

    public SokeOrd(String[] liste){
        ordliste = new HashSet<String>();
        ordliste.addAll(Arrays.asList(liste));
    }

    public boolean leggTilSokeOrd(String ord){
        return ordliste.add(ord);
    }

    public boolean leggTilSokeOrd(String[] ord){
        String[] cpord = new String[ord.length];
        for(int i = 0; i < cpord.length; i++){
            String meld = ord[i];
            if(meld != null){
                meld.toLowerCase();
                cpord[i] = meld;
            }
        }
        return ordliste.addAll(Arrays.asList(cpord));
    }

    public HashSet<String> getOrdliste() {
        return ordliste;
    }
}
