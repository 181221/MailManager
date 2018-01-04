package no.pederyo.klient;

import no.pederyo.Attributter;
import no.pederyo.grensesnitt.Grensesnitt;
import no.pederyo.mailmanager.EmailSearcher;
import no.pederyo.mailmanager.SokeOrd;
import no.pederyo.protokoll.implementasjon.Imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static no.pederyo.klient.manager.ManagerHelper.opprettLytter;

/**
 * Created by Peder on 04.01.2018.
 */
public class CSVSkriver {
    private static final String FILE_HEADER = "SokeListe,Beskrivelse,framappe,tilmappe";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_DELIMITER = ",";
    private static final String FILNAVN = "settings.csv";

    public static FileWriter startWriter() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FILNAVN, true);
            BufferedReader fileReader = new BufferedReader(new FileReader(FILNAVN));
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

    public static void lesFraSokeord(Imap imap){
        BufferedReader fileReader = null;
        try {
            ArrayList<String> sokeord = new ArrayList<>();
            String line = "";
            fileReader = new BufferedReader(new FileReader(FILNAVN));
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
                System.out.println("Oppretter lytter...");
                // LISTENER
                opprettLytter(fra, til , emailSearcher);
                dance();
            }
            } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void dance() throws InterruptedException {
        for (int i = 1; i < 20; i ++){
            Thread.sleep(100);
            String printut = "|                 |\r";
            for(int k = 0; k < i; k ++){
                printut += '=';
            }
            System.out.print(printut +"\n");
        }
    }
}
