package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;
public class Pop3Protokoll implements IProtokoll {

    private Properties pop3Properties;
    private Store store;

    public Pop3Protokoll(){

    }

    public Properties setupProperties() {
        String host = "pop.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.user", Attributter.FRAMAIL);
        properties.put("mail.password", Attributter.PASSORD);
        return properties;
    }
    private Store pop3Store(){
        Session emailSession = Session.getDefaultInstance(pop3Properties);
        Store store = null;
        try {
            store = emailSession.getStore("pop3s");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }

    public Session authenticate() {
        return null;
    }
}
