package no.pederyo.klient;

import biz.source_code.crypto.IdeaFileEncryption;

import java.io.*;

/**
 * Created by Peder on 04.01.2018.
 */
public class KlientEncrypt {
    public static void main(String[] args) throws IOException {
        PrintWriter writer = null;
        File temp = null;
        try {
            writer = new PrintWriter("bruker.csv", "UTF-8");
            temp = File.createTempFile("tempfile", ".csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            bw.write("This is the temporary file content");
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
