package no.pederyo.klient.manager;

import no.pederyo.Attributter;
import no.pederyo.dataaccess.BrukerEAO;
import no.pederyo.dataaccess.EmailEAO;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.klient.meny.Meny;
import no.pederyo.modell.Bruker;
import no.pederyo.modell.Email;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;
import no.pederyo.util.CSVSkriverUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

import static no.pederyo.Attributter.BRUKER_FILNAVN;
import static no.pederyo.Attributter.SETTINGS_FILNAVN;

public class ManagerKlient {

    private static Scanner in = new Scanner(System.in);
    private static Imap imap;
    private static Smtp smtp;
    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args)  {
        String menu = "1: Opprett en mailklient bruker\n2: Logginn\n3: Avslutt";
        int valg;
        String brukernavn;
        Bruker b;
        emf = Persistence.createEntityManagerFactory("hibernate");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    in.nextLine();

                    System.out.print("Skriv inn brukernavn");
                    brukernavn = in.nextLine();
                    //valider
                    b = BrukerEAO.getBruker(em, brukernavn);
                    if(b != null) {
                        System.out.println("Det eksisterer allerede en bruker med det navnet");
                    }else {
                        b = new Bruker();
                        b.setName(brukernavn);
                        BrukerEAO.insertBruker(em, b);
                    }
                    break;

                   /* if(MenyHjelper.harProfil()){
                        System.out.println("Du har allerede en klient.");
                    }else {
                        opprettKlient();
                    }
                    break*/
                case 2:
                    System.out.println("skriv inn brukernavn");
                    in.nextLine();
                    brukernavn = in.nextLine();

                    b= BrukerEAO.getBruker(em, brukernavn);
                    if(b != null) {
                        if(b.getEmailsById() == null){
                            //Opprett mailklient
                            opprettMailklient(em, b);
                        }else{
                            for(Email e : b.getEmailsById()){
                                System.out.println("Id "+ "("+e.getId()+")" +" "+ e.getUsername());
                            }
                            int id = Integer.parseInt(in.nextLine());
                            Email e = EmailEAO.getMail(em, id);
                            Imap imap = new Imap(e.getMailtype());
                            Meny.lytterMenu(imap);
                        }

                        //Meny.lytterMenu();
                    }
                    break;
                   /* if(!MenyHjelper.harProfil())
                        System.out.println("Du har ingen Klienter. Vennligst opprett en.");
                    else{
                        String mappe = MenyHjelper.velgBrukerFil();
                        CSVSkriverUtil.hentBrukerInfoFrafil(mappe);
                        imap = new Imap(Attributter.FRATYPE);
                        Meny.lytterMenu(imap);*/


            }

        } while(valg != 3);
    }

    private static void opprettMailklient(EntityManager em, Bruker b) {

        int mailtype = grensesnitt.velgMailType();

        loggInnOgConnect(mailtype);

        String salt = BCrypt.gensalt(12);

        String generatedSecuredPasswordHash = BCrypt.hashpw(Attributter.PASSORD, salt);
        Attributter.setPASSORD(generatedSecuredPasswordHash);
        Attributter.setSALT(salt);

        EmailEAO.opprettMail(em, b);
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
