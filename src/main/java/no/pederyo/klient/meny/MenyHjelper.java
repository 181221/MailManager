package no.pederyo.klient.meny;

import java.io.File;
import java.util.Scanner;

public class MenyHjelper {

    private static Scanner in = new Scanner(System.in);


    public static boolean harProfil(){
        File files[] = new File(".").listFiles();
        boolean finnes = false;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && !(file.getName().startsWith("settings"))) {
                finnes = true;
            }
        }
        return finnes;
    }


    public static String velgBrukerFil(){
        File files[] = new File(".").listFiles();
        String mappe = null;
        System.out.println("Velg mappe..");
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && !file.getName().startsWith("settings")) {
                System.out.println(file.getName());
                mappe = file.getName();
            }
        }
        mappe = velgMappe(files);
        return mappe;
    }


    public static String velgSettingsFil(){
        File files[] = new File(".").listFiles();
        String mappe = null;
        boolean finnes = false;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv") && file.getName().startsWith("settings")) {
                System.out.println(file.getName());
                finnes = true;
            }
        }
        if(!finnes){
            System.out.println("Du har ingen configs");
            return null;
        }
        mappe = velgMappe(files);
        return mappe;
    }


    public static String velgMappe(File[] files){
        boolean ok = false;
        String mappe = null;
        while(!ok){
            mappe = in.nextLine();
            for (File file : files) {
                if(mappe.equals(file.getName())){
                    ok = true;
                }
            }
            if(!ok){
                System.out.println("Skriv inn riktig.");
            }
        }
        return mappe;
    }



}
