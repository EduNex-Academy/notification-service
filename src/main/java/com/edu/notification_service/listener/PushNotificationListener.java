package com.edu.notification_service.listener;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class PushNotificationListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "push-notification-topic", groupId = "notification-service")
    public void handlePushNotification(@Valid NotificationRequest request) {
        try {
            MDC.put("notificationType", request.getType().name());
            MDC.put("recipient", request.getRecipient());

            if (request.getType().name().startsWith("ERROR") || request.getType().name().startsWith("WARNING")) {
                log.warn("Processing high-priority notification - Type: {}, Recipient: {}, Message: {}",
                        request.getType(),
                        request.getRecipient(),
                        request.getMessage().substring(0, Math.min(50, request.getMessage().length())));
            } else {
                log.info("Processing notification - Type: {}, Recipient: {}, Message preview: {}",
                        request.getType(),
                        request.getRecipient(),
                        request.getMessage().substring(0, Math.min(50, request.getMessage().length())));
            }

            var notification = notificationService.sendNotification(request);

            log.info("Successfully processed notification [ID: {}] for recipient: {}",
                    notification.getId(),
                    request.getRecipient());

        } catch (Exception e) {
            log.error("Failed to process notification - Type: {}, Recipient: {}, Error: {}",
                    request.getType(),
                    request.getRecipient(),
                    e.getMessage(), e);
            throw e; // Re-throw to trigger Kafka retry mechanism
        } finally {
            MDC.clear();
        }
    }
}
