package no.pederyo.crypt;

import biz.source_code.crypto.IdeaFileEncryption;
import no.pederyo.Attributter;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Peder on 04.01.2018.
 */
public class Krypterer {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "username,password,mailtype";


    public static void main(String[] args) {
        String filnavn = "hvlmail";
        skrivTilFil(1,filnavn);
        hentInfoFrafil(filnavn);
    }

    public static void hentInfoFrafil(String filnavn) {
        deKrypter(filnavn);
    }

    public static void skrivTilFil(int mailtype, String bruker) {
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
            krypter(temp, bruker);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void krypter(File temp, String bruker){
        try {
            PrintWriter writer = new PrintWriter(bruker +".csv", "UTF-8");
            writer.close();
            IdeaFileEncryption.cryptFile(String.valueOf(temp), bruker +".csv", "sesame", true, IdeaFileEncryption.Mode.CBC);
            temp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean deKrypter(String filnavn){
        File temp = null;
        try {
            temp = File.createTempFile("1234112", ".csv");
            IdeaFileEncryption.cryptFile(filnavn, String.valueOf(temp), "sesame", false, IdeaFileEncryption.Mode.CBC);
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(temp));
            br.readLine();
            String[] linje = br.readLine().split(",");
            Attributter.setFRAMAIL(linje[0]);
            Attributter.setPASSORD(linje[1]);
            Attributter.setFRATYPE(Integer.parseInt(linje[2]));
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp.delete();
    }
}
