package no.pederyo;

import java.util.*;

public class Attributter {
    //       ----  Innlogging  ----
    public static final String
            FRAMAIL = System.getenv("MAIL_FRA"),
            PASSORD = System.getenv("MAIL_PW");

    // DEFAULT SokeOrd
    public static final String[] SOKEORD = new String[] {"kvittering",
            "ordre", "ordrebekreftelse",
            "bekreftelse", "receipt", "reisedokumenter"};



}
