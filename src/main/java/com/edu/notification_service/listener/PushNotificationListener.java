package com.edu.notification_service.listener;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.domain.NotificationType;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class PushNotificationListener {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final Set<NotificationType> HIGH_PRIORITY_TYPES = Set.of(
        NotificationType.ERROR,
        NotificationType.WARNING,
        NotificationType.SYSTEM_ALERT
    );

    @KafkaListener(topics = "push-notification-topic", groupId = "notification-group")
    public void handlePushNotification(@Valid NotificationRequest request) {
        try {
            MDC.put("notificationType", request.getType().name());
            MDC.put("recipient", request.getRecipient());

            // Log based on notification priority
            if (HIGH_PRIORITY_TYPES.contains(request.getType())) {
                log.warn("Processing high-priority notification - Type: {}, Recipient: {}, Message: {}",
                        request.getType(),
                        request.getRecipient(),
                        truncateMessage(request.getMessage()));
            } else {
                log.info("Processing notification - Type: {}, Recipient: {}, Message preview: {}",
                        request.getType(),
                        request.getRecipient(),
                        truncateMessage(request.getMessage()));
            }

            // Persist notification and get the result
            Object result = notificationService.sendNotification(request);
            if (!(result instanceof Notification)) {
                throw new IllegalStateException("Expected Notification object but got: " + result.getClass());
            }
            Notification notification = (Notification) result;

            // Send real-time notification via WebSocket
            String destination = String.format("/topic/notifications/%s", request.getRecipient());
            messagingTemplate.convertAndSend(destination, notification);

            log.info("Successfully processed and delivered notification [ID: {}] for recipient: {}",
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

    private String truncateMessage(String message) {
        return message.substring(0, Math.min(50, message.length()));
    }
}
