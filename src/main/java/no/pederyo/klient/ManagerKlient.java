package no.pederyo.klient;

import no.pederyo.Lytter.Lytter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Peder on 03.01.2018.
 */
public class ManagerKlient {

    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);
    private static Imap imap;

    public static void main(String[] args) throws MessagingException {
        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    statiskFerdigKlient();
                    break;
                case 2:
                    break;
                case 4:
                    break;
            }
        }while(valg != 3);
    }

    private static void statiskFerdigKlient() throws MessagingException {
        imap = new Imap();
        System.out.println("Sett opp søke liste");
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        // MAPPE
        System.out.println("Venligst velg en mappe fra mailen din.");
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = grensesnitt.velgMappe(map, imap);
        System.out.println("Velg mappe du vil flytte meldinger til.");
        Folder til = grensesnitt.velgMappe(map, imap);
        fra.open(Folder.READ_WRITE);
        til.open(Folder.READ_WRITE);

        System.out.println("Oppretter Søker...");
        // SØKER
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        // BEHANDLER
        System.out.println("Oppretter lytter...");
        // LISTENER
        Lytter lytter = new Lytter(fra, til, 60000, emailSearcher);
        Thread thread = new Thread(lytter);
        thread.start();
    }
}
