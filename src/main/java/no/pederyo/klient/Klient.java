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
        String menu = "1: Soeker\n2: OpprettSoeker \n3: VisSokere \n4: LeggTil en Behandler\n5: Avslutt";
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
                        MailUtil mu = opprettBehandler();
                }
            }while(valg != 5);
            }
    }

    private static MailUtil opprettBehandler() {
        System.out.println("Velg 2 mapper en fra og en til mappe.");
        Folder mappe1 = velgMappeBehandler();
        Folder mappe2 = velgMappeBehandler();
        return new MailUtil(mappe1, mappe2);
    }
    private static Folder velgMappeBehandler(){
        int i = 0;
        HashMap<Integer, String> map = new HashMap<>();
        for(Folder f : imap.getAllFolders()){
            map.put(i,f.getName());
            System.out.println("(" + i + ") " +f.getName());
            i++;
        }
        return imap.getFolder(map.get(in.nextInt()));
    }

    private static void soekerMenu(EmailSearcher em) {

    }


    private static EmailSearcher opprettSoeker() {
        SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());
        System.out.println("Velg Mappe du vil søke i ");
        Folder folder = velgMappeBehandler();
        try {
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        EmailSearcher em = new EmailSearcher(sokeOrd, folder, imap);
        System.out.println("Legg til en beskrivelse");
        em.setBeskrivelse(in.next());
        emailSearcher.add(em);
        return em;
    }

    private static EmailSearcher soeker() {
        if(emailSearcher == null){
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


    private static void statiskFerdigKlient() {
        imap = new Imap();
    }

    private static void hentKleint() {
    }


    private static void opprettKlient() throws MessagingException {
        int mailtype = grensesnitt.velgMailType();
        grensesnitt.skrivInnMail();
        boolean ok = grensesnitt.loggInn();
        imap = new Imap(mailtype);
        smtp = new Smtp(mailtype);
        if(imap.isHarConnected()){
            SokeOrd sokeOrd = new SokeOrd(grensesnitt.opprettSokeListe());

            // MAPPE
            String mappenavn = grensesnitt.velgMappe();
            Folder mappe = grensesnitt.opprettMappe(imap, mappenavn);
            Folder inbox = imap.getFolder("INBOX");
            mappe.open(Folder.READ_WRITE);
            inbox.open(Folder.READ_WRITE);

            System.out.println("Oppretter Søker...");
            // SØKER
            EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, inbox, imap);
            System.out.println("Oppretter behandler..");
            // BEHANDLER
            MailUtil mailUtil = new MailUtil(inbox, mappe);

            System.out.println("Oppretter lytter...");
            // LISTENER
            Lytter lytter = new Lytter(inbox, 60000, emailSearcher, mailUtil);

            Thread thread = new Thread(lytter);

            thread.start();

        }else {
            System.out.println("Passord eller brukernavn er feil.");
            grensesnitt.loggInn();
        }

    }


}
