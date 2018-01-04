package no.pederyo.klient.manager;

import no.pederyo.Attributter;
import no.pederyo.Lytter.Lytter;
import no.pederyo.crypt.Krypterer;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.klient.CSVSkriver;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.FileWriter;
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


    public static void mineLyttere() {
        for (Lytter l : lyttere) {
            System.out.println(l.getEmailSearcher().getBeskrivelse());
        }
    }


    public static void setupKlient() throws MessagingException, InterruptedException {
        ArrayList<String> listetilcsv = new ArrayList<>();

        System.out.println("Legg til en søke liste");
        String[] sokeord = grensesnitt.opprettSokeListe();
        SokeOrd sokeOrd = new SokeOrd(sokeord);

        System.out.println("Vennligst skriv inn en beskrivelse til søkemotoren.");
        String beskrivelse = in.nextLine();
        listetilcsv.add(beskrivelse);

        // MAPPE
        System.out.println("Venligst velg en mappe fra mailen din.");
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = grensesnitt.velgMappe(map, imap);
        System.out.println("Velg mappe du vil flytte meldinger til.");
        Folder til = grensesnitt.velgMappe(map, imap);
        listetilcsv.add(fra.getName());
        listetilcsv.add(til.getName());
        fra.open(Folder.READ_WRITE);
        til.open(Folder.READ_WRITE);

        Thread.sleep(1000);

        // SKRIVER

        CSVSkriver.skrivTilcsv(CSVSkriver.startWriter(), listetilcsv, sokeord);

        // SØKER
        System.out.println("Oppretter Søker...");
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        emailSearcher.setBeskrivelse(beskrivelse);

        Thread.sleep(1000);

        // LISTENER
        System.out.println("Oppretter lytter...");
        opprettLytter(fra, til , emailSearcher);
        dance();
    }

    private static void dance() throws InterruptedException {
        for (int i = 1; i < 10; i ++){
            Thread.sleep(100);
            String printut = "|                 |\r";
            for(int k = 0; k < i; k ++){
                printut += '=';
            }
            System.out.print(printut);
        }
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
        System.out.println("skriv inn et navn til filen");
        String filnavn = in.nextLine();
        Krypterer.skrivTilFil(mailtype, filnavn);
    }
    public static void ferdigKlient() {
        imap = new Imap();
    }
    public static void ferdigKlient2(){
        imap = new Imap(Attributter.FRATYPE);
    }

    public static void endreLytter(Lytter lytter) {
        String menu = "1: Søkeord \n2: Mapper \n3 Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    ManagerHelper.mineLyttere();
                    break;
                case 2:
                    Lytter l = ManagerHelper.hentLytter();
                    ManagerHelper.endreLytter(l);
                    break;
            }
        }while(valg != 3);
    }

    public static Lytter hentLytter() {
        return null;
    }

    public static void slettLytter(Lytter lytter) {
    }


    public static void organisermeny() {
        String menu = "1: Mine Lyttere\n2: Endre Lytter \n3: Lag ny Lytter \n4: Slett Lytter \n5 Organiser \n6 Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    System.out.println("Legg til søkeord i slkelisten");
                    SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }while(valg != 6);

    }
}
