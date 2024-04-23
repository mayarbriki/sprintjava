package com.example.demo3;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private static final String USERNAME = "mayar.briki@esprit.tn";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        String toEmail = "recipient@example.com"; // Change to recipient's email address
        try {
            sendEmailConfirmation(toEmail);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public static void sendEmailConfirmation(String toEmail) throws MessagingException {
        Session session = createSession();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Confirmation de commande");
            message.setText("Votre commande a été confirmée avec succès!");

            Transport.send(message);
            System.out.println("Email confirmation sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e; // Re-throw the exception for higher-level handling if needed
        }
    }

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }
}
