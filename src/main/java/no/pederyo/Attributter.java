package no.pederyo;


public class Attributter {
    //       ----  Innlogging  ----

    public static String
            FRAMAIL = System.getenv("MAIL_FRA"),
            PASSORD = System.getenv("MAIL_PW");

    // DEFAULT SokeOrd
    public static final String[] SOKEORD = new String[] {"kvittering",
            "ordre", "ordrebekreftelse",
            "bekreftelse", "receipt", "reisedokumenter"};

    private static String bruker, passord;


    public static void setFRAMAIL(String FRAMAIL) {
        Attributter.FRAMAIL = FRAMAIL;
    }

    public static void setPASSORD(String PASSORD) {
        Attributter.PASSORD = PASSORD;
    }


}
