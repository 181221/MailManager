package no.pederyo.util;
import no.pederyo.Attributter;
import no.pederyo.klient.manager.ManagerHelper;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.modell.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Peder on 04.01.2018.
 */
public class CSVSkriverUtil {


    private static final String FILE_HEADER = "SokeListe,Beskrivelse,framappe,tilmappe";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_DELIMITER = ",";
    private static String FIL;


    public static FileWriter startWriter(String navn) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(navn, true);
            BufferedReader fileReader = new BufferedReader(new FileReader(navn));
            if(fileReader.readLine() == null){
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileWriter;
    }


    public static void skrivTilcsv(FileWriter fw, ArrayList<String> liste, String[] bewritten){
        try {
            for (String ord : bewritten){
                fw.append(ord + " ");
            }
            fw.append(COMMA_DELIMITER);
            for(String s : liste) {
                fw.append(s);
                fw.append(COMMA_DELIMITER);
            }
            fw.append(NEW_LINE_SEPARATOR);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void hentBrukerInfoFrafil(String filnavn) {
        KryptererUtil.deKrypter(filnavn);
    }


    public static boolean hentLyttereFraFil(Imap imap, String filnavn){
        BufferedReader fileReader = null;
        try {
            ArrayList<String> sokeord = new ArrayList<>();
            String line = "";
            fileReader = new BufferedReader(new FileReader(filnavn));
            fileReader.readLine();

            while ((line = fileReader.readLine()) != null) {
                String[] info = line.split(",");
                String[] sokeordene = info[0].split(" ");
                String beskrivelse = info[1];
                String fraMappe = info[2];
                String tilMappe = info[3];
                SokeOrd sokeOrd = new SokeOrd(sokeordene);
                Folder fra = imap.getFolder(fraMappe);
                Folder til = imap.getFolder(tilMappe);
                fra.open(Folder.READ_WRITE);
                til.open(Folder.READ_WRITE);
                EmailSearcher emailSearcher = new EmailSearcher(sokeOrd, fra, imap);
                emailSearcher.setBeskrivelse(beskrivelse);
                Thread.sleep(1000);
                System.out.println("Oppretter lytter... for " + emailSearcher.getBeskrivelse());
                // LISTENER
                ManagerHelper.opprettLytter(fra, til , emailSearcher);

            }
            } catch (FileNotFoundException e) {
            System.out.println("Du har ikke en config ennå. Oppretter en...");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static void skrivBrukerTilDb(int mailtype, String bruker) {


    }

    public static void skrivBrukerTilFil(int mailtype, String bruker) {
        File temp = null;
        try {
            temp = File.createTempFile("123", ".csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            bw.append(FILE_HEADER);
            bw.append(NEW_LINE_SEPARATOR);
            bw.append(Attributter.FRAMAIL);
            bw.append(COMMA_DELIMITER);
            bw.append(Attributter.PASSORD);
            bw.append(COMMA_DELIMITER);
            bw.append(String.valueOf(mailtype));
            bw.close();
            KryptererUtil.krypter(temp, bruker);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void lagConfig(String filnavn) {
        FIL = filnavn;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FIL, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void dance() throws InterruptedException {
        for (int i = 1; i < 10; i ++){
            Thread.sleep(100);
            String printut = "|                 |\r";
            for(int k = 0; k < i; k ++){
                printut += '=';
            }
            System.out.print(printut);
        }
    }
}
