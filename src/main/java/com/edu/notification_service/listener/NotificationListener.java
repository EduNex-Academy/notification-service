package com.edu.notification_service.listener;

import com.edu.notification_service.dto.EmailRequest;
import com.edu.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final EmailService emailService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void handleNotification(EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
    }
}
