package no.pederyo.protokoll;

import no.pederyo.Attributter;

import javax.mail.*;
import java.util.Properties;
public class Pop3 implements IProtokoll {

    private Properties properties;
    private Store store;
    private Session session;

    public Pop3(){
        properties = setup();
        store = Store();
        session = authenticate();
    }

    public Properties setup() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", Attributter.POP3HOST);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.user", Attributter.FRAMAIL);
        properties.put("mail.password", Attributter.PASSORD);
        return properties;
    }

    public Session authenticate() {
        return Session.getInstance(this.properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Attributter.FRAMAIL, Attributter.PASSORD);
            }
        });
    }

    private Store Store(){
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = null;
        try {
            store = emailSession.getStore("pop3s");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return store;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

}
