package no.pederyo.klient.manager;

import javax.mail.MessagingException;
import java.util.Scanner;

/**
 * Created by Peder on 03.01.2018.
 */
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
        String menu = "1: Dine Lyttere\n2: Endre Lytter \n3: LeggTil Lytter \n4: Slett Lytter\n5: velg behandler \n6 Legg til lytter \n7 Avslutt";
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
                    case 6:
                        break;
                }
            }while(valg != 7);
    }


}
