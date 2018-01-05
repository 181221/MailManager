package no.pederyo.modell;

import no.pederyo.Attributter;

import java.util.Arrays;
import java.util.HashSet;

public class SokeOrd {

    private volatile HashSet<String> ordliste;

    public SokeOrd(){
        ordliste = new HashSet<String>();
    }

    public SokeOrd(String[] liste){
        ordliste = new HashSet<String>();
        for(String ord : liste){
            ordliste.add(ord.toLowerCase());
        }
    }

    public boolean leggTilSokeOrd(String ord){
        return ordliste.add(ord.toLowerCase());
    }

    public boolean leggTilSokeOrd(String[] ord){
        String[] cpord = new String[ord.length];
        for(int i = 0; i < cpord.length; i++){
            String meld = ord[i];
            if(meld != null){
                meld = meld.toLowerCase();
                cpord[i] = meld;
            }
        }
        return ordliste.addAll(Arrays.asList(cpord));
    }

    public HashSet<String> getOrdliste() {
        return ordliste;
    }
    public void visListe() {
        for(String ord : ordliste){
            System.out.println(ord);
        }
    }
}
