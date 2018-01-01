package no.pederyo.protokoll;

import javax.mail.Store;

/**
 * Created by Peder on 30.12.2017.
 */
public interface IConnect {
    Store store();

    void connect();

    void close();

}
