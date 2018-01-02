package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.Lytter.Lytter;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.MailUtil;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.IImap;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.protokoll.implementasjon.Smtp;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class Klient {

    public static void main(String[] args) throws MessagingException {
            // PROTOKOLL
            int mailtype = skrivInnMailtype();
            authorisering();
            Imap imap = new Imap(mailtype);
            Smtp smtp = new Smtp(mailtype);

            // SETUP SOKEORD
            String[] liste = Attributter.SOKEORD;
            SokeOrd sokeOrd = new SokeOrd(liste);
            // MAPPE
            Folder Kvitteringer = imap.getFolder("Kvitteringer");
            Folder inbox = imap.getFolder("INBOX");
            Kvitteringer.open(Folder.READ_WRITE);
            inbox.open(Folder.READ_WRITE);

            // SØKER
            EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, inbox, imap);

            // BEHANDLER
            MailUtil mailUtil = new MailUtil(inbox, Kvitteringer);

            // LISTENER
            Lytter lytter = new Lytter(inbox, 60000, emailSearcher, mailUtil);

            Thread thread = new Thread(lytter);

            thread.start();


            emailSearcher.hentMeldingerFraSokeListe();
            mailUtil.printUt(emailSearcher.hentMeldingerFraSokeListe());
    }

    public static int skrivInnMailtype(){
            System.out.println("Velg Mail Klient");

            System.out.println("(" + 0 + ")" + Imap.IMAP_GMAIL_COM);

            System.out.println("(" + 1 + ")" + Imap.IMAP_OUTLOOK_COM);

            System.out.println("(" + 2 + ")" + Imap.IMAP_YAHOO_COM);

            Scanner in = new Scanner(System.in);

            System.out.println("..");
            return in.nextInt();

    }
    public static void authorisering() {
            char password[] = null;
            try {
                    password = PasswordField.getPassword(System.in, "Enter your password: ");
            } catch(IOException ioe) {
                    ioe.printStackTrace();
            }
            if(password == null ) {
                    System.out.println("No password entered");
            } else {
                    System.out.println("The password entered is: "+String.valueOf(password));
            }
    }
}
