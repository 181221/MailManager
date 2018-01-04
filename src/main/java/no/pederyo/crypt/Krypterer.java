package no.pederyo.crypt;

import biz.source_code.crypto.IdeaFileEncryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Peder on 04.01.2018.
 */
public class Krypterer {

    public static void krypter(File temp){
        try {
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
