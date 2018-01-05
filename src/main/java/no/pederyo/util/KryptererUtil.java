package no.pederyo.util;

import biz.source_code.crypto.IdeaFileEncryption;
import no.pederyo.Attributter;

import java.io.*;
/**
 * Created by Peder on 04.01.2018.
 */
public class KryptererUtil {

    static void krypter(File temp, String bruker){
        try {
            PrintWriter writer = new PrintWriter(bruker +".csv", "UTF-8");
            writer.close();
            IdeaFileEncryption.cryptFile(String.valueOf(temp), bruker +".csv", "sesame", true, IdeaFileEncryption.Mode.CBC);
            temp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static boolean deKrypter(String filnavn){
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
