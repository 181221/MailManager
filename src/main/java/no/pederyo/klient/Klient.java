package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.Lytter.Lytter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Klient {
    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);
    private static Imap imap;
    private static Smtp smtp;
    private static SokeOrd sokeOrd;
    private static ArrayList<EmailSearcher> emailSearcher = new ArrayList<>();
    private static ArrayList<MailUtil> mailUtils = new ArrayList<>();

    public static void main(String[] args) throws MessagingException {

        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    opprettKlient();
                    startKlient();
                    break;
                case 2:
                    hentKleint();
                    startKlient();
                    break;
                case 4:
                    statiskFerdigKlient();
                    startKlient();
                    break;
            }
        }while(valg != 3);
    }

    private static void startKlient() throws MessagingException {
        String menu = "1: Soeker\n2: OpprettSoeker \n3: VisSokere \n4: LeggTil en Behandler\n5: velg behandler \n6 Legg til lytter \n7 Avslutt";
        int valg;
        if(imap.isHarConnected()){
            do {
                System.out.println(menu);
                valg = in.nextInt();
                switch (valg){
                    case 1:
                        EmailSearcher em = soeker();
                        soekerMenu(em);
                        break;
                    case 2:
                        EmailSearcher emails = opprettSoeker();
                        soekerMenu(emails);
                        break;
                    case 3:
                        visListe();
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        EmailSearcher soker = soeker();
                        System.out.println("Velg Behandler");
                        if(soker != null){
                            System.out.println("Oppretter Lytter");
                            // LISTENER
                        }else {
                            System.out.println("det skjedde en feil");
                            break;
                        }
                }
            }while(valg != 7);
            }
    }

    private static void soekerMenu(EmailSearcher em) {

    }
    private static EmailSearcher opprettSoeker() {
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        System.out.println("Velg Mappe du vil søke i ");
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = null;
        try {
            fra = grensesnitt.velgMappe(map, imap);
            fra.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        EmailSearcher em = new EmailSearcher(sokeOrd, fra, imap);
        System.out.println("Legg til en beskrivelse for denne søkeren");
        in.nextLine();
        em.setBeskrivelse(in.nextLine());
        emailSearcher.add(em);
        return em;
    }

    private static EmailSearcher soeker() {
        if(emailSearcher == null || emailSearcher.isEmpty()){
            System.out.println("Du har ingen søkere. Vennligst Opprett en.");
        }else {
            System.out.println("velg soker");
            int i = 0;
            for(EmailSearcher es : emailSearcher){
                System.out.println("EmailSøker " + "(" + i + ")" + es.getBeskrivelse());
                i++;
            }
            return emailSearcher.get(in.nextInt());
        }
        return null;
    }

    private static void sokeliste() {
        String menu = "1: Vis sokeliste\n2: legg til ord i sokelisten \n3: Opprett en ny sokeliste \n4: Tilbake";
        int valg;
        if(imap.isHarConnected()){
            do {
                System.out.println(menu);
                valg = in.nextInt();
                switch (valg){
                    case 1:
                        visListe();
                        break;
                    case 2:

                        break;
                    case 4:
                        break;
                }
            }while(valg != 4);
        }

    }

    private static void visListe() {
        if(emailSearcher == null || emailSearcher.isEmpty()){
            System.out.println("Din liste er tom lag en ny liste");
        }else {
            for(EmailSearcher es : emailSearcher){
                es.getSokeOrd().visListe();
            }
        }
    }


    private static void statiskFerdigKlient() throws MessagingException {
        imap = new Imap();
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        // MAPPE
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = grensesnitt.velgMappe(map, imap);
        Folder til = grensesnitt.velgMappe(map, imap);
        fra.open(Folder.READ_WRITE);
        fra.open(Folder.READ_WRITE);

        System.out.println("Oppretter Søker...");
        // SØKER
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        System.out.println("Oppretter behandler..");
        // BEHANDLER
        System.out.println("Oppretter lytter...");
        // LISTENER
        Lytter lytter = new Lytter(fra, til, 60000, emailSearcher);
        Thread thread = new Thread(lytter);
        thread.start();
    }

    private static void hentKleint() {

    }


    private static void opprettKlient() throws MessagingException {
        int mailtype = grensesnitt.velgMailType();
        grensesnitt.skrivInnMail();
        grensesnitt.loggInn();
        imap = new Imap(mailtype);
        smtp = new Smtp(mailtype);
        while(!imap.isHarConnected()){
            System.out.println("Passord eller brukernavn er feil.");
            grensesnitt.loggInn();
        }
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        // MAPPE
        HashMap<Integer, String> map = grensesnitt.visMappe(imap);
        Folder fra = grensesnitt.velgMappe(map, imap);
        Folder til = grensesnitt.velgMappe(map, imap);
        fra.open(Folder.READ_WRITE);
        fra.open(Folder.READ_WRITE);

        System.out.println("Oppretter Søker...");
        // SØKER
        EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
        System.out.println("Oppretter behandler..");
        // BEHANDLER
        System.out.println("Oppretter lytter...");
        // LISTENER
        Lytter lytter = new Lytter(fra, til, 60000, emailSearcher);
        Thread thread = new Thread(lytter);
        thread.start();

    }


}
