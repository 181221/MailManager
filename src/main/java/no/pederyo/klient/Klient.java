package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.Lytter.Lytter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.String.valueOf;

public class Klient {
    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws MessagingException {

        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    opprettKlient();
                    break;
                case 2:
                    hentKleint();
                    break;
                case 4:
                    statiskFerdigKlient();
                    break;
            }
        }while(valg != 3);
    }

    private static void statiskFerdigKlient() {
        Imap imap = new Imap();
    }

    private static void hentKleint() {
    }


    private static void opprettKlient() throws MessagingException {
        int mailtype = grensesnitt.velgMailType();
        grensesnitt.skrivInnMail();
        boolean ok = grensesnitt.loggInn();
        Imap imap = new Imap(mailtype);
        Smtp smtp = new Smtp(mailtype);
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
