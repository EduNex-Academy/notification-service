package com.edu.notification_service.listener;

import com.edu.notification_service.dto.SubscriptionEvent;
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
public class SubscriptionEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "subscription-events-topic", groupId = "notification-group")
    public void handleSubscriptionEvent(@Valid SubscriptionEvent event) {
        try {
            MDC.put("eventType", event.getEventType());
            MDC.put("userId", event.getUserId());
            MDC.put("subscriptionId", event.getSubscriptionId());

            log.info("\uD83D\uDCE9 Received subscription event: {} for user {}", event.getEventType(), event.getUserId());

            // Delegate to NotificationService
            notificationService.handleSubscriptionEvent(event);

            if (event.getNotificationType().toString().contains("ERROR") ||
                event.getNotificationType().toString().contains("WARNING")) {
                log.warn("\u26A0\uFE0F Subscription alert for user {}: {}", event.getUserId(), event.getEventType());
            } else {
                log.info("\u2705 Successfully processed subscription event for user {}", event.getUserId());
            }
        } catch (Exception e) {
            log.error("\u274C Error processing subscription event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
