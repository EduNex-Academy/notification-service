package com.edu.notification_service.listener;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventConsumer.class);
    private final NotificationService notificationService;

    public KafkaEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void handleNotification(@Payload NotificationRequest request) {
        logger.info("Received notification request for user: {}, type: {}",
                   request.getUserId(), request.getType());
        notificationService.processNotification(request);
    }

    @KafkaListener(topics = "email-notification-topic", groupId = "notification-group")
    public void handleEmailNotification(@Payload NotificationRequest request) {
        logger.info("Received email notification request for: {}", request.getRecipient());
        notificationService.processEmailNotification(request);
    }
}
