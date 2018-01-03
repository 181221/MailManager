package no.pederyo.klient;

import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Klient {
    private static Imap imap;
    private static Grensesnitt grensesnitt = new Grensesnitt();
    private static Scanner in = new Scanner(System.in);
    private static ArrayList<EmailSearcher> emailSearcher = new ArrayList<>();

    public static void main(String[] args) throws MessagingException, InterruptedException {

        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    break;
                case 2:
                    break;
                case 4:
                    break;
            }
        }while(valg != 3);
    }


    private static void soekerMenu(EmailSearcher em) {

    }
    private static EmailSearcher opprettSoeker() throws InterruptedException {

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
        System.out.println("Legg til en beskrivelse ");
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


    private static void visListe() {
        if(emailSearcher == null || emailSearcher.isEmpty()){
            System.out.println("Din liste er tom lag en ny liste");
        }else {
            for(EmailSearcher es : emailSearcher){
                es.getSokeOrd().visListe();
            }
        }
    }


}
