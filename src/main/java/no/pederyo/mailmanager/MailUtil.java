package no.pederyo.mailmanager;

import no.pederyo.modell.Mail;
import no.pederyo.protokoll.Pop3;
import no.pederyo.protokoll.Smtp;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

public class MailUtil {

    public static String send(Mail mail, Smtp smtp) {
        try {
            MimeMessage message = new MimeMessage(smtp.getSession());
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

    public static void check(Pop3 pop3){
        try {
            Store store = pop3.getStore();
            store.connect();
            Folder emailFolder = store.getFolder("INBOX");
            printUtMeldinger(emailFolder);
            emailFolder.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUtMeldinger(Folder emailFolder) throws MessagingException, IOException {
        emailFolder.open(Folder.READ_ONLY);
        Message[] messages = emailFolder.getMessages();
        System.out.println("messages.length---" + messages.length);

        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            System.out.println("---------------------------------");
            System.out.println("Email Number " + (i + 1));
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + message.getContent().toString());

        }
    }

}
