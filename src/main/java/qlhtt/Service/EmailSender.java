package qlhtt.Service;


import org.springframework.boot.autoconfigure.jms.JmsProperties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets; 
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class EmailSender {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "duythvo2004@gmail.com";
        String recipientEmail = "vothaiduy19092004@gmail.com";
        String password = "kxrc qvlv vlrg yfar";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });
        String html = new String(Files.readAllBytes(Paths.get("src/main/resources/template.html")), StandardCharsets.UTF_8);

        Message message = prepareMessage(session, myEmail, recipientEmail, "Thông tin hóa đơn", html);

        try {
            Transport.send(message);
            System.out.println("Message sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String myEmail, String recipientEmail, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html ; charset=utf-8");
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
