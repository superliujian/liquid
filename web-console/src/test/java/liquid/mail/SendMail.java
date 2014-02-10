package liquid.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by redbrick9 on 2/10/14.
 */
public class SendMail {
    public static void main(String[] args) {
        // Recipient's email ID needs to be mentioned.
        String to = "taoma09@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "liquid_admin@163.com";

        // Assuming you are sending email from localhost
        String host = "smtp.163.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.user", "liquid_admin@163.com");
        properties.setProperty("mail.password", "liquid");

        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
        Session session = Session
                .getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("liquid_admin@163.com", "liquid");
                    }
                });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("管理员测试");

            // Now set the actual message
            message.setText("欢迎使用集装箱管理系统！");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
