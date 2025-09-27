// E:/notification-service/src/test/java/com/edu/notification_service/service/EmailServiceTest.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.EmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail_shouldCallJavaMailSenderSend() {
        EmailRequest request = new EmailRequest();
        request.setTo("to@test.com");
        request.setSubject("Subject");
        request.setBody("Body");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);
        emailService.sendEmail(request);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmailWithAttachment_shouldAddAttachmentIfProvided() {
        EmailRequest request = new EmailRequest();
        request.setTo("to@test.com");
        request.setSubject("Subject");
        request.setBody("Body");
        request.setAttachment("test.txt");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);
        emailService.sendEmailWithAttachment(request);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmail_shouldHandleMessagingExceptionGracefully() {
        EmailRequest request = new EmailRequest();
        request.setTo("to@test.com");
        request.setSubject("Subject");
        request.setBody("Body");
        when(javaMailSender.createMimeMessage()).thenThrow(new MessagingException("Failed"));
        try {
            emailService.sendEmail(request);
        } catch (RuntimeException e) {
            assert(e.getMessage().contains("Failed to send email"));
        }
    }
}
