package no.pederyo.crypt;

import biz.source_code.crypto.IdeaFileEncryption;
import no.pederyo.Attributter;

import java.io.*;

/**
 * Created by Peder on 04.01.2018.
 */
public class Krypterer {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "username,password,mailtype";
    public static void lesFraFil(){
        deKrypter();
    }

    public static void main(String[] args) {
        skrivTilFil(1);
        deKrypter();
    }

    public static void skrivTilFil(int mailtype) {
        File temp = null;
        try {
            temp = File.createTempFile("123", ".csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            Attributter.setPASSORD("hei");
            Attributter.setFRAMAIL("hei");
            bw.append(FILE_HEADER);
            bw.append(NEW_LINE_SEPARATOR);
            bw.append(Attributter.FRAMAIL);
            bw.append(COMMA_DELIMITER);
            bw.append(Attributter.PASSORD);
            bw.append(COMMA_DELIMITER);
            bw.append(String.valueOf(mailtype));
            bw.close();
            krypter(temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void krypter(File temp){
        try {
            PrintWriter writer = new PrintWriter("bruker.csv", "UTF-8");
            writer.close();
            IdeaFileEncryption.cryptFile(String.valueOf(temp), "bruker.csv", "sesame", true, IdeaFileEncryption.Mode.CBC);
            temp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean deKrypter(){
        File temp = null;
        try {
            temp = File.createTempFile("1234112", ".csv");
            IdeaFileEncryption.cryptFile("bruker.csv", String.valueOf(temp), "sesame", false, IdeaFileEncryption.Mode.CBC);
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(temp));
            String linje = null;
            while ((linje= br.readLine()) != null) {
                System.out.println(linje);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp.delete();
    }
}
