package no.pederyo.klient.meny;

import no.pederyo.klient.manager.ManagerHelper;
import no.pederyo.mailmanager.Lytter;
import no.pederyo.protokoll.implementasjon.Imap;
import no.pederyo.util.CSVSkriverUtil;

import javax.mail.MessagingException;
import java.util.Scanner;

public class Meny {

    private static Scanner in = new Scanner(System.in);


    public static void lytterMenu(Imap imap) {
        String menu = "1: Mine Lyttere\n2: HentLytter fra Fil \n3: Lag ny Lytter \n4: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    ManagerHelper.mineLyttere();
                    break;
                case 2:
                    System.out.println("Velg mappe..");
                    String mappe = MenyHjelper.velgSettingsFil();
                    if(mappe == null){
                        System.out.println("Vennligst opprett en fil");
                    }else {
                        CSVSkriverUtil.hentLyttereFraFil(imap, mappe);
                    }
                    break;
                case 3:
                    try {
                        ManagerHelper.setupKlient(imap);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }while(valg != 4);
    }



    public static void organisermeny() {
        String menu = "1: Mine Lyttere\n2: Endre Lytter \n3: Lag ny Lytter \n4: Slett Lytter \n5 Organiser \n6 Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }while(valg != 6);
    }


    public static void endreLytter(Lytter lytter) {
        String menu = "1: Søkeord \n2: Mapper \n3 Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:
                    ManagerHelper.mineLyttere();
                    break;
                case 2:
                    Lytter l = ManagerHelper.hentLytter();
                    //ManagerHelper.endreLytter(l);
                    break;
            }
        }while(valg != 3);
    }
}
