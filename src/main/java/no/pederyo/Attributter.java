package no.pederyo;

public class Attributter {
    //       ----  Innlogging  ----
    public static final String
            FRAMAIL = System.getenv("MAIL_FRA"),
            PASSORD = System.getenv("MAIL_PW");

    //       ----   Hoster     ----
    public static final String POP3HOST = "pop.gmail.com", SMTPHOST = "smtp.gmail.com", IMAPHOST="imap.gmail.com";

    //       ---- PORTS ----

    // SokeOrd
    public static final String[] SOKEORD = new String[] {"kvittering","ordre","ordrebekreftelse","bekreftelse","receipt", "reisedokumenter"};



}
