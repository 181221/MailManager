package no.pederyo.protokoll;

import javax.mail.Session;
import java.util.Properties;

public interface IProtokoll {
    Properties setupProperties();
    Session authenticate();
}
