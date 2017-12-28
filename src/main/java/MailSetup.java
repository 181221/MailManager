
import javax.mail.*;
import java.util.Properties;

public class MailSetup {
    private Properties smtpProperties;
    private Properties pop3Properties;
    private Session mailSession;

    public MailSetup(){
        smtpProperties = setUpSMTPProperties();
        pop3Properties = setupPop3Proterties();
        mailSession = Authenticate();
    }

    private Properties setupPop3Proterties() {
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = null;
        try {
            store = emailSession.getStore("pop3s");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            if (store != null) {
                store.connect(host, Attributter.FRAMAIL, Attributter.PASSORD);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private Properties setUpSMTPProperties() {
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.user", Attributter.FRAMAIL);
        props.put("mail.password", Attributter.PASSORD);
        props.put("mail.port", "465");
        return props;
    }

    private Session Authenticate() {
        return Session.getInstance(this.smtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Attributter.FRAMAIL, Attributter.PASSORD);
            }
        });
    }


    public Session getMailSession() {
        return mailSession;
    }

    public Properties getSmtpProperties() {
        return smtpProperties;
    }

    public Properties getPop3Properties() {
        return pop3Properties;
    }
}

