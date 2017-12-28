package no.pederyo.mailmanager;

import no.pederyo.Attributter;
import no.pederyo.modell.Mail;
import no.pederyo.protokoll.Imap;
import no.pederyo.protokoll.Pop3;
import no.pederyo.protokoll.Smtp;

import javax.mail.*;
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
    public static void check(Imap imap) {
        try {
            imap.connect();
            imap.store().getFolder("Inbox");

            Folder[] emailFolder = imap.store().getDefaultFolder().list("*");
            System.out.println(emailFolder.length);
            imap.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printUtAlleMeldinger(Pop3 pop3, String type){
        try {
            Store store = pop3.getStore();
            store.connect(Attributter.POP3HOST, Attributter.FRAMAIL, Attributter.PASSORD);
            Folder emailFolder = store.getFolder("Inbox");
            printUtMeldinger(emailFolder);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUtMeldinger(Folder emailFolder) throws MessagingException, IOException {
        emailFolder.open(Folder.READ_ONLY);
        emailFolder.getFullName();
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
