package no.pederyo.klient.manager;

import no.pederyo.Lytter.Lytter;
import no.pederyo.crypt.Krypterer;

import javax.mail.MessagingException;
import java.util.Scanner;

public class ManagerKlient {

    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws MessagingException, InterruptedException {
        String menu = "1: Opprett en mailklient\n2: Last inn en mailklient\n3: Avslutt";
        int valg;
        do {
            System.out.println(menu);
            valg = in.nextInt();
            switch (valg){
                case 1:

                    Krypterer.skrivTilFil(1);
                    Krypterer.deKrypter();
                    System.out.println("hei");
                    ManagerHelper.opprettKlient();
                    ManagerHelper.setupKlient();
                    managerMenu();
                    break;
                case 2:
                    ManagerHelper.ferdigKlient();
                    ManagerHelper.setupKlient();
                    managerMenu();
                    break;
            }
        }while(valg != 3);
    }

    private static void managerMenu() {
        String menu = "1: Mine Lyttere\n2: Endre Lytter \n3: Lag ny Lytter \n4: Slett Lytter \n5 Organiser \n6 Avslutt";
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
                        ManagerHelper.endreLytter(l);
                        break;
                    case 3:
                        try {
                            ManagerHelper.setupKlient();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        ManagerHelper.slettLytter(ManagerHelper.hentLytter());
                        break;
                    case 5:
                        ManagerHelper.organisermeny();
                        break;
                }
            }while(valg != 6);
    }



}
