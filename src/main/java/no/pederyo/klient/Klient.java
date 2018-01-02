package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.Lytter.Lytter;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.String.valueOf;

public class Klient {
    public static boolean loggInn(){
        Attributter.setFRAMAIL(skrivInnMail());
        Boolean godkjent = authorisering();
        while (!godkjent) {
            godkjent = authorisering();
        }
        return godkjent;
    }
    public static Imap quickSetup() {
        Attributter.setFRAMAIL(Attributter.FRAMAIL);
        Attributter.setPASSORD(Attributter.PASSORD);
        return new Imap();
    }

    public static void main(String[] args) throws MessagingException {
        // PROTOKOLL
        System.out.println("lol");
        /*int mailtype = skrivInnMailtype();
        loggInn();
        Imap imap = new Imap(mailtype);
        Smtp smtp = new Smtp(mailtype);
        boolean ok = false;
        while (!ok){
            if(!imap.isHarConnected()){
                System.out.println("Skriv inn på nytt.");
                ok = loggInn();
                System.out.println(ok);
            }else {
                ok = true;
            }
        }*/
        Imap imap = quickSetup();
        // SETUP SOKEORD
        //String[] liste = Attributter.SOKEORD;
        SokeOrd sokeOrd = new SokeOrd(opprettSokeListe());
        // MAPPE
        String mappenavn = velgMappe();
        Folder mappe;
        if(!(imap.getFolder(mappenavn).exists())){
            mappe = imap.opprettFolder(mappenavn);
        }else {
            mappe = imap.getFolder(mappenavn);
        }
        mappe.open(Folder.READ_WRITE);
        Folder inbox = imap.getFolder("INBOX");
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

        emailSearcher.hentMeldingerFraSokeListe();
        mailUtil.printUt(emailSearcher.hentMeldingerFraSokeListe());
    }

    private static String skrivInnMail() {
        System.out.println("Skriv inn mail example@exaple.com");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    private static String velgMappe() {
        System.out.println("Velg en mappe hvor meldingene skal flyttes til.");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }


    public static int skrivInnMailtype() {
        System.out.println("Velg Mail Klient");

        System.out.println("(" + 0 + ")" + Imap.IMAP_GMAIL_COM);

        System.out.println("(" + 1 + ")" + Imap.IMAP_OUTLOOK_COM);

        System.out.println("(" + 2 + ")" + Imap.IMAP_YAHOO_COM);

        Scanner in = new Scanner(System.in);

        System.out.println("..");
        return in.nextInt();

    }
    public static String[] opprettSokeListe(){
        System.out.println("\nSkriv inn søkeord skilt med mellomrom..");
        Scanner in = new Scanner(System.in);
        return in.nextLine().split(" ");

    }

    public static boolean godkjenn(char[] pass, char[] pass1) {
        return valueOf(pass).equals(valueOf(pass1));
    }

    public static boolean authorisering() {
        char password[] = null;
        char password1[] = null;
        boolean ok = false;
        try {
            password = PasswordField.getPassword(System.in, "Enter your password: ");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (password == null) {
            System.out.println("No password entered");
        } else {
            try {
                password1 = PasswordField.getPassword(System.in, "Enter your password: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (password1 == null) {
                System.out.println("No password entered");
            } else {
                if (godkjenn(password1, password)) {
                    Attributter.setPASSORD(String.valueOf(password));
                    return true;
                }
            }
        }
        return false;
    }
}
