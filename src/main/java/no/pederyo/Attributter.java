package no.pederyo;


import java.util.Arrays;
import java.util.HashSet;

public class Attributter {
    //       ----  Innlogging  ----

    public static String
            FRAMAIL = System.getenv("MAIL_FRA"),
            PASSORD = System.getenv("MAIL_PW");


    public static int FRATYPE = -1;



    public static String BRUKER_FILNAVN, SETTINGS_FILNAVN;



    public static String TYPE = "Gmail";

    public static HashSet<String> GmailDefaultMapper = new HashSet<>(Arrays.asList(
            new String[]{"Drafts","Important","All Mail","Sent Mail","Starred","Trash"}));







    public static void setBrukerFilnavn(String brukerFilnavn) {
        BRUKER_FILNAVN = brukerFilnavn;
    }

    public static void setSettingsFilnavn(String settingsFilnavn) {
        SETTINGS_FILNAVN = settingsFilnavn;
    }

    public static void setFRATYPE(int FRATYPE) {
        Attributter.FRATYPE = FRATYPE;
    }

    public static void setFRAMAIL(String FRAMAIL) {
        Attributter.FRAMAIL = FRAMAIL;
    }

    public static void setPASSORD(String PASSORD) {
        Attributter.PASSORD = PASSORD;
    }


}
