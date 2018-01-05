package no.pederyo.klient.manager;

import no.pederyo.Lytter.Lytter;
import no.pederyo.crypt.Krypterer;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.klient.CSVSkriver;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.MessagingException;
import java.io.File;
import java.util.Scanner;

import static no.pederyo.klient.manager.ManagerHelper.BRUKER_FILNAVN;

public class ManagerKlient {

    private static Scanner in = new Scanner(System.in);
    private static Imap imap;
    public static String BRUKER_FILNAVN, SETTINGS_FILNAVN;

    private static Smtp smtp;

    private static Grensesnitt grensesnitt = new Grensesnitt();

    public static void main(String[] args) throws MessagingException, InterruptedException {
        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    if(harProfil()){
                        System.out.println("Du har allerede en klient.");
                    }else {
                        opprettKlient();
                    }
                    break;
                case 2:
                    if(!harProfil())
                        System.out.println("Du har ingen Klienter. Vennligst opprett en.");
                    else{
                        String mappe = velgBrukerFil();
                        Krypterer.hentInfoFrafil(mappe);
                        imap = new Imap();
                        lytterMenu();
                    }
                    break;
                case 4:
                    Krypterer.skrivTilFil(1, "filnavn");
            }
        }while(valg != 3);
    }

    private static void lytterMenu() {
        String menu = "1: Mine Lyttere\n2: HentLytter fra Fil \n3: Lag ny Lytter \n4: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    ManagerHelper.mineLyttere();
                    break;
                case 2:
                    System.out.println("Velg mappe..");
                    String mappe = velgSettingsFil();
                    if(mappe == null){
                        System.out.println("Vennligst opprett en fil");
                    }else {
                        CSVSkriver.lesFraSokeord(imap, mappe);
                    }
                    break;
                case 3:
                    try {
                        ManagerHelper.setupKlient(imap);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }while(valg != 4);
    }

    private static boolean harProfil(){
        File files[] = new File(".").listFiles();
        boolean finnes = false;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && !(file.getName().startsWith("settings"))) {
                finnes = true;
            }
        }
        return finnes;
    }
    private static String velgBrukerFil(){
        File files[] = new File(".").listFiles();
        String mappe = null;
        System.out.println("Velg mappe..");
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && !file.getName().startsWith("settings")) {
                System.out.println(file.getName());
                mappe = file.getName();
            }
        }
        mappe = velgMappe(files);
        return mappe;
    }
    private static String velgSettingsFil(){
        File files[] = new File(".").listFiles();
        String mappe = null;
        boolean finnes = false;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && file.getName().startsWith("settings")) {
                System.out.println(file.getName());
                finnes = true;
            }
        }
        if(!finnes){
            System.out.println("Du har ingen configs");
            return null;
        }
        mappe = velgMappe(files);
        return mappe;
    }
    public static String velgMappe(File[] files){
        boolean ok = false;
        String mappe = null;
        while(!ok){
            mappe = in.nextLine();
            for (File file : files) {
                if(mappe.equals(file.getName())){
                    ok = true;
                }
            }
            if(!ok){
                System.out.println("Skriv inn riktig.");
            }
        }
        return mappe;
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
        in.nextLine();
        System.out.println("skriv inn et navn til filen");
        BRUKER_FILNAVN = in.nextLine();
        while(BRUKER_FILNAVN == null || BRUKER_FILNAVN.equals(" ") || BRUKER_FILNAVN.isEmpty()){
            System.out.println("Skriv inn et gyldig navn");
            BRUKER_FILNAVN = in.nextLine();
        }
        Krypterer.skrivTilFil(mailtype, BRUKER_FILNAVN);
        SETTINGS_FILNAVN = "settings_" + BRUKER_FILNAVN;
        if(!SETTINGS_FILNAVN.endsWith(".csv")){
            SETTINGS_FILNAVN += ".csv";
        }
        CSVSkriver.lagConfig(SETTINGS_FILNAVN);
    }

}
