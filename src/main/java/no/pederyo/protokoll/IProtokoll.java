package no.pederyo.protokoll;

import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public interface IProtokoll {

    Properties setup();

    Session authenticate();

    Store store();

    void connect();

    void close();
}
