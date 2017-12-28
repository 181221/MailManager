package no.pederyo.mail;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
    private static String send(Mail mail, MailSetup mailSetup) {
        try {
            MimeMessage message = new MimeMessage(mailSetup.getMailSession());
            message.setFrom(new InternetAddress(mail.getFra()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTil()));
            message.setSubject(mail.getSubject());
            message.setText(mail.getMsg());
            javax.mail.Transport.send(message);
            mail.setResult("Your mail sent successfully....");
        } catch (AddressException e) {
            mail.setResult(e.getMessage());
        } catch (javax.mail.MessagingException e) {
            mail.setResult(e.getMessage());
        }
        return mail.getResult();
    }

}
