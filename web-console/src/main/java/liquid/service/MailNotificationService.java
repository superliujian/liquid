package liquid.service;

import liquid.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by redbrick9 on 2/18/14.
 */
public class MailNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(MailNotificationService.class);

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void sendToAdmin(String subject, String content) {
        send(subject, content, properties.getProperty("mail.user"));
    }

    public void send(String subject, String content, String... mailTo) {
        Session session = Session
                .getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(properties.getProperty("mail.user")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(properties.getProperty("mail.user")));

            if (null != mailTo) {
                // Set To: header field of the header.
                for (String mailToAddress : mailTo) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailToAddress));
                }
            }
            // message.addRecipient(Message.RecipientType.CC, new InternetAddress((mailTo)));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);
            logger.debug("Mail has been successfully sent to {}", StringUtils.toString(mailTo));
        } catch (MessagingException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
