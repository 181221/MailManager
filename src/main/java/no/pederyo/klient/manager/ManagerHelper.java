package no.pederyo.klient.manager;

import no.pederyo.Attributter;
import no.pederyo.mailmanager.Lytter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.modell.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.util.CSVSkriverUtil;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.*;

/**
 * Created by Peder on 03.01.2018.
 */
public class ManagerHelper {


    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);

    public static ArrayList<Lytter> lyttere = new ArrayList<>();
    public static ArrayList<Thread> traader = new ArrayList<>();


    public static void mineLyttere() {
        for (Lytter l : lyttere) {
            System.out.println(l.getEmailSearcher().getBeskrivelse());
        }
    }


    public static void setupKlient(Imap imap) throws MessagingException, InterruptedException {
        // SOKEORD

        ArrayList<String> listetilcsv = new ArrayList<>();

        System.out.println("Legg til en søke liste");

        String[] sokeord = grensesnitt.opprettSokeListe();

        SokeOrd sokeOrd = new SokeOrd(sokeord);

        String beskrivelse = LeggTilBeksrivelse(listetilcsv);

        // MAPPE
        System.out.println("Venligst velg en mappe fra mailen din.");

        HashMap<Integer, String> map = grensesnitt.visMappe(imap);

        Folder fra = opprettMappe(imap, map);

        System.out.println("Velg mappe du vil flytte meldinger til.");

        Folder til = opprettMappe(imap, map);

        leggTilIListe(listetilcsv, fra, til);

        // SOKER OG LYTTER

        Thread.sleep(1000);

        opprettSokerOgLytter(sokeOrd, fra, til, imap, beskrivelse);

        // SKRIVER

        CSVSkriverUtil.skrivTilcsv(CSVSkriverUtil.startWriter(Attributter.SETTINGS_FILNAVN), listetilcsv, sokeord);

    }


    private static void opprettSokerOgLytter(SokeOrd sokeOrd, Folder fra, Folder til, Imap imap, String beskrivelse) throws InterruptedException {
        // SØKER
        System.out.println("Oppretter Søker...");

        EmailSearcher es = opprettEmailSearcher(sokeOrd, fra, imap, beskrivelse);

        Thread.sleep(1000);

        // LISTENER
        System.out.println("Oppretter lytter...");

        opprettLytter(fra, til , es);

        CSVSkriverUtil.dance();
    }

    private static String LeggTilBeksrivelse(ArrayList<String> listetilcsv) {
        System.out.println("Vennligst skriv inn en beskrivelse til søkemotoren.");
        String beskrivelse = in.nextLine();
        listetilcsv.add(beskrivelse);
        return beskrivelse;
    }

    private static void leggTilIListe(ArrayList<String> listetilcsv, Folder fra, Folder til){
        listetilcsv.add(fra.getName());
        listetilcsv.add(til.getName());
    }


    private static Folder opprettMappe(Imap imap, HashMap<Integer, String> map) throws MessagingException, InterruptedException {
        Folder folder = grensesnitt.velgLytterMappe(map, imap);
        if(Attributter.GmailDefaultMapper.contains(folder.getName())){
            folder = imap.getFolder("[" + Attributter.TYPE + "]/"+folder.getName());
        }
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    private static EmailSearcher opprettEmailSearcher(SokeOrd sokeOrd, Folder fra, Imap imap, String beskrivelse) {
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        emailSearcher.setBeskrivelse(beskrivelse);
        return emailSearcher;
    }


    public static void opprettLytter(Folder fra, Folder til, EmailSearcher es){
        Lytter lytter = new Lytter(fra, til, 60000, es);
        Thread thread = new Thread(lytter);
        thread.start();
        lyttere.add(lytter);
        traader.add(thread);
    }


    public static Lytter hentLytter() {
        return null;
    }

    public static void slettLytter(Lytter lytter) {
    }



}
