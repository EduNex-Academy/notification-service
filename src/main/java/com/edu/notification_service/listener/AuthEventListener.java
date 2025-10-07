package com.edu.notification_service.listener;

import com.edu.notification_service.dto.AuthEvent;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

import jakarta.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class AuthEventListener {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "auth-events-topic", groupId = "notification-group")
    public void handleAuthEvent(@Valid AuthEvent event) {
        try {
            MDC.put("actionType", event.getActionType());
            MDC.put("userId", event.getUserId());

            log.info("🔐 Received auth event: {} for user {}", event.getActionType(), event.getUserId());

            // Create notification from auth event
            Notification notification = new Notification();
            notification.setUserId(Long.valueOf(event.getUserId()));
            notification.setType(event.getNotificationType());
            notification.setMessage(event.getMessage());
            notification.setTitle(String.format("Authentication %s", event.getActionType()));
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReadFlag(false);
            notification.setRecipient(event.getEmail());

            notification = notificationService.saveNotification(notification);

            // Send real-time notification via WebSocket
            String destination = "/topic/notifications/" + event.getUserId();
            messagingTemplate.convertAndSend(destination, notification);

            if (event.getNotificationType().toString().contains("ERROR") ||
                event.getNotificationType().toString().contains("WARNING")) {
                log.warn("⚠️ Security alert for user {}: {}", event.getUserId(), event.getActionType());
            } else {
                log.info("✅ Successfully processed auth event for user {}", event.getUserId());
            }
        } catch (Exception e) {
            log.error("❌ Error processing auth event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
