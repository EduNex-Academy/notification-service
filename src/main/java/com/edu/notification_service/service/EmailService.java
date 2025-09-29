// src/main/java/com/edu/notification_service/service/EmailService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(EmailRequest request) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, request.getAttachment() != null);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true);

            if (request.getAttachment() != null) {
                File attachmentFile = new File(request.getAttachment());
                if (attachmentFile.exists()) {
                    helper.addAttachment(attachmentFile.getName(), attachmentFile);
                }
            }

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendEmailWithAttachment(EmailRequest request) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true);
            // Attachments logic can be added here
            if (request.getAttachment() != null) {
                File file = new File(request.getAttachment());
                if (file.exists()) {
                    helper.addAttachment(file.getName(), file);
                } else {
                    throw new RuntimeException("Attachment file not found: " + request.getAttachment());
                }
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
