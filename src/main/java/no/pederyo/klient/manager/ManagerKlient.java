package no.pederyo.klient.manager;

import no.pederyo.Attributter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.klient.meny.Meny;
import no.pederyo.klient.meny.MenyHjelper;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;
import no.pederyo.util.CSVSkriverUtil;
import java.util.Scanner;

import static no.pederyo.Attributter.BRUKER_FILNAVN;
import static no.pederyo.Attributter.SETTINGS_FILNAVN;

public class ManagerKlient {

    private static Scanner in = new Scanner(System.in);
    private static Imap imap;
    private static Smtp smtp;
    private static Grensesnitt grensesnitt = new Grensesnitt();

    public static void main(String[] args)  {
        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    if(MenyHjelper.harProfil()){
                        System.out.println("Du har allerede en klient.");
                    }else {
                        opprettKlient();
                    }
                    break;
                case 2:
                    if(!MenyHjelper.harProfil())
                        System.out.println("Du har ingen Klienter. Vennligst opprett en.");
                    else{
                        String mappe = MenyHjelper.velgBrukerFil();
                        CSVSkriverUtil.hentBrukerInfoFrafil(mappe);
                        imap = new Imap(Attributter.FRATYPE);
                        Meny.lytterMenu(imap);
                    }
                    break;
                case 4:
                    CSVSkriverUtil.skrivBrukerTilFil(1, "filnavn");
            }

        } while(valg != 3);
    }


    private static void opprettKlient() {

        int mailtype = grensesnitt.velgMailType();

        loggInnOgConnect(mailtype);

        in.nextLine();

        System.out.println("Skriv inn et navn til filen");

        lagBrukerfil();

        lagSettingsfil();

        CSVSkriverUtil.skrivBrukerTilFil(mailtype, BRUKER_FILNAVN);

        CSVSkriverUtil.lagConfig(SETTINGS_FILNAVN);
    }

    private static void loggInnOgConnect(int mailtype){
        grensesnitt.skrivInnMail();
        grensesnitt.loggInn();

        imap = new Imap(mailtype);
        smtp = new Smtp(mailtype);
        while(!imap.isHarConnected()){
            System.out.println("Passord eller brukernavn er feil.");
            grensesnitt.loggInn();
        }
    }

    private static void lagBrukerfil(){
        BRUKER_FILNAVN = in.nextLine();
        while(BRUKER_FILNAVN == null || BRUKER_FILNAVN.equals(" ") || BRUKER_FILNAVN.isEmpty()){
            System.out.println("Skriv inn et gyldig navn");
            BRUKER_FILNAVN = in.nextLine();
        }
    }

    private static void lagSettingsfil(){
        SETTINGS_FILNAVN = "settings_" + BRUKER_FILNAVN;
        if(!SETTINGS_FILNAVN.endsWith(".csv")){
            SETTINGS_FILNAVN += ".csv";
        }
    }



}
