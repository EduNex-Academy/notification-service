package com.edu.notification_service.listener;

import com.edu.notification_service.dto.SubscriptionEvent;
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
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class SubscriptionEventListener {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "subscription-events-topic", groupId = "notification-group")
    public void handleSubscriptionEvent(@Valid SubscriptionEvent event) {
        try {
            MDC.put("eventType", event.getEventType());
            MDC.put("userId", event.getUserId());
            MDC.put("subscriptionId", event.getSubscriptionId());

            log.info("📩 Received subscription event: {} for user {}", event.getEventType(), event.getUserId());

            // Create notification from subscription event
            Notification notification = new Notification();
            notification.setUserId(Long.valueOf(event.getUserId()));
            notification.setType(event.getNotificationType());
            notification.setMessage(event.getMessage());
            notification.setTitle(String.format("Subscription %s", event.getEventType().toLowerCase()));
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReadFlag(false);

            // Add subscription metadata
            try {
                String metadata = objectMapper.writeValueAsString(
                    Map.of("subscriptionId", event.getSubscriptionId(),
                          "eventType", event.getEventType())
                );
                notification.setMetadataJson(metadata);
            } catch (Exception e) {
                log.warn("Failed to serialize subscription metadata", e);
            }

            notification = notificationService.saveNotification(notification);

            // Send real-time notification via WebSocket
            String destination = "/topic/notifications/" + event.getUserId();
            messagingTemplate.convertAndSend(destination, notification);

            log.info("✅ Successfully processed subscription event: {} for subscription {}",
                    event.getEventType(), event.getSubscriptionId());
        } catch (Exception e) {
            log.error("❌ Error processing subscription event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
