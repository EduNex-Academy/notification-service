// src/main/java/com/edu/notification_service/service/EmailService.java
package com.edu.notification_service.service;

import com.edu.notification_service.model.EmailDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDetails details) {
        logger.info("Sending email to: {}", details.getRecipient());
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getBody(), true);

            if (details.getAttachment() != null) {
                File attachmentFile = new File(details.getAttachment());
                if (attachmentFile.exists()) {
                    helper.addAttachment(attachmentFile.getName(), attachmentFile);
                    logger.info("Added attachment: {}", attachmentFile.getName());
                } else {
                    logger.warn("Attachment file not found: {}", details.getAttachment());
                }
            }

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", details.getRecipient());
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", details.getRecipient(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
