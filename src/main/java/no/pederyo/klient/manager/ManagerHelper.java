package no.pederyo.klient.manager;

import no.pederyo.Lytter.Lytter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Peder on 03.01.2018.
 */
public class ManagerHelper {

    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);
    private static Imap imap;
    private static Smtp smtp;
    private static ArrayList<Lytter> lyttere = new ArrayList<>();
    private static ArrayList<Thread> traader = new ArrayList<>();

    public static void setupKlient() throws MessagingException, InterruptedException {
        System.out.println("Legg til en søke liste");
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        System.out.println("Vennligst skriv inn en beskrivelse til søkemotoren.");
        in.nextLine();
        String beskrivelse = in.nextLine();
        System.out.println("Venligst velg en mappe fra mailen din.");
        // MAPPE
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = grensesnitt.velgMappe(map, imap);
        System.out.println("Velg mappe du vil flytte meldinger til.");
        Folder til = grensesnitt.velgMappe(map, imap);
        fra.open(Folder.READ_WRITE);
        til.open(Folder.READ_WRITE);

        System.out.println("Oppretter Søker...");
        // SØKER
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        emailSearcher.setBeskrivelse(beskrivelse);

        System.out.println("Oppretter lytter...");
        // LISTENER
        opprettLytter(fra, til , emailSearcher);

    }

    public static void opprettLytter(Folder fra, Folder til, EmailSearcher es){
        Lytter lytter = new Lytter(fra, til, 60000, es);
        Thread thread = new Thread(lytter);
        thread.start();
        lyttere.add(lytter);
        traader.add(thread);
    }
    public static void opprettKlient() {
        int mailtype = grensesnitt.velgMailType();
        grensesnitt.skrivInnMail();
        grensesnitt.loggInn();
        imap = new Imap(mailtype);
        smtp = new Smtp(mailtype);
        while(!imap.isHarConnected()){
            System.out.println("Passord eller brukernavn er feil.");
            grensesnitt.loggInn();
        }
    }
    public static void ferdigKlient() {
        imap = new Imap();
    }

}
