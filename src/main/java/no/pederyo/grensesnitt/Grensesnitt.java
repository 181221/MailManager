package no.pederyo.grensesnitt;

import no.pederyo.Attributter;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

public class Grensesnitt {

    private Scanner in = new Scanner(System.in);

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

    private int parseInt(){
        int i = -1;
        try {
            i = Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("feil prøv igjen.");
        }
        return i;
    }
    private Folder tryGetFolder(Imap imap, HashMap<Integer,String> map, int i) {
        Folder folder = null;
            try {
                folder = imap.getFolder(map.get(i));
            }catch (NullPointerException e){
                System.out.println("mappen eksisterer ikke skriv på nytt." + e.getMessage());
            }
        return folder;
    }

    public Folder velgMappe(HashMap<Integer, String> map, Imap imap) throws MessagingException, InterruptedException {
        int i = parseInt();
        while(i == -1){
            i = parseInt();
        }
        Folder folder = null;
        if(i == 0){
            System.out.println("Skriv inn navn på mappen");
            String navn = in.nextLine();
            while(navn == null){
                System.out.println("Skriv inn et gyldig navn");
                navn = in.nextLine();
            }
            System.out.println("Oppretter mappen..");
            Thread.sleep(200);
            folder = imap.opprettFolder(navn);
        }else {
            folder = tryGetFolder(imap, map, i);
            while (folder == null){
                System.out.println("mappen finnes ikke. Prøv på nytt.");
                i = parseInt();
                folder = tryGetFolder(imap, map, i);
            }
        }
        return folder;
    }
    public HashMap<Integer, String> visMappe(Imap imap) throws InterruptedException {
        Thread.sleep(300);
        int i = 1;
        HashMap<Integer, String> map = new HashMap<>();

        System.out.println("(" + 0 + ") Opprett ny");
        for(Folder f : imap.getAllFolders()){
            Thread.sleep(100);
            map.put(i, f.getName());
            System.out.println("(" + i + ") " + f.getName());
            i++;
        }
        return map;
    }
    public String[] opprettSokeListe(){
        System.out.println("Skriv inn søkeord skilt med mellomrom..");
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
