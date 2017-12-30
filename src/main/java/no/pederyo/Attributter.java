package no.pederyo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Attributter {
    //       ----  Innlogging  ----
    public static final String
            FRAMAIL = System.getenv("MAIL_FRA"),
            PASSORD = System.getenv("MAIL_PW");

    //       ----   Hoster     ----
    public static final String POP_GMAIL_COM = "pop.gmail.com",
            SMTP_GMAIL_COM = "smtp.gmail.com",
            IMAP_GMAIL_COM = "imap.gmail.com",
            IMAP_OUTLOOK_COM ="" ;

    public static final Set<String> host = new HashSet<String>(Arrays.asList(
            new String[] {POP_GMAIL_COM, SMTP_GMAIL_COM, IMAP_GMAIL_COM,}));

    //       ---- PORTS ----

    // SokeOrd
    public static final String[] SOKEORD = new String[] {"kvittering","ordre","ordrebekreftelse","bekreftelse","receipt", "reisedokumenter"};



}
