package no.pederyo.grensesnitt;

import no.pederyo.Attributter;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

public class Grensesnitt {

    Scanner in = new Scanner(System.in);

    private String passordInput(){
        Console cons = System.console();
        char[] passwd = cons.readPassword("[%s]", "Password:");
        String passord = String.valueOf(passwd);
        if ((cons) != null &&
                (passwd != null)) {
            java.util.Arrays.fill(passwd, '*');
        }
        return passord;
    }
    public int velgMailType(){
        System.out.println("Velg Mail Klient");

        System.out.println("(" + 1 + ") " + Imap.IMAP_GMAIL_COM);

        System.out.println("(" + 2 + ") " + Imap.IMAP_OUTLOOK_COM);

        System.out.println("(" + 3 + ") " + Imap.IMAP_YAHOO_COM);

        int klient = in.nextInt();

        return klient - 1;
    }

    public boolean loggInn(){
        Boolean godkjent = authorisering();
        while (!godkjent) {
            godkjent = authorisering();
        }
        return godkjent;
    }
    public String skrivInnMail() {
        System.out.println("Skriv inn din mail <example@exaple.com>");
        String mail = in.nextLine();
        mail = in.nextLine();
        Attributter.setFRAMAIL(mail);
        return mail;
    }
    private boolean passValidering(String pass) {
        return pass != null && !pass.equals(" ");
    }

    private boolean authorisering() {
        String password = passordInput();
        String password1 = passordInput();
        if (passValidering(password) && passValidering(password1)) {
            if (godkjenn(password1, password)) {
                Attributter.setPASSORD(String.valueOf(password));
                return true;
            }else {
                System.out.println("Passord stemmer ikke");
            }
        } else {
            System.out.println("No password entered");
        }
        return false;
    }

    private static boolean godkjenn(String pass, String pass1) {
        return pass.equals(pass1);
    }

    public Folder velgMappe(HashMap<Integer, String> map, Imap imap) throws MessagingException {
        System.out.println("Vennligst velg en mappe");
        int i = in.nextInt();
        Folder folder = null;
        if(i == 0){
            System.out.println("Skriv inn navn på mappen");
            String navn = in.next();
            folder = imap.opprettFolder(navn);
        }else {
            folder = imap.getFolder(map.get(in.nextInt()));
        }
        return folder;
    }
    public HashMap<Integer, String> visMappe(Imap imap){
        int i = 1;
        HashMap<Integer, String> map = new HashMap<>();
        for(Folder f : imap.getAllFolders()){
            map.put(i, f.getName());
            i++;
        }
        System.out.println("(" + 0 + ") Opprett ny");
        return map;
    }
    public String[] opprettSokeListe(){
        System.out.println("\nSkriv inn søkeord skilt med mellomrom..");
        return in.nextLine().split(" ");

    }
    public Imap quickSetup() {
        Attributter.setFRAMAIL(Attributter.FRAMAIL);
        Attributter.setPASSORD(Attributter.PASSORD);
        return new Imap();
    }
    public Folder opprettMappe(Imap imap, String mappenavn) throws MessagingException {
        Folder mappe;
        if(!(imap.getFolder(mappenavn).exists())){
            mappe = imap.opprettFolder(mappenavn);
        }else {
            mappe = imap.getFolder(mappenavn);
        }
        return mappe;
    }

}
