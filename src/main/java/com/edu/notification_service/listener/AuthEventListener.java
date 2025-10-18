package com.edu.notification_service.listener;

import com.edu.notification_service.dto.AuthEvent;
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
public class AuthEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "auth-events-topic", groupId = "notification-group")
    public void handleAuthEvent(@Valid AuthEvent event) {
        try {
            MDC.put("actionType", event.getActionType());
            MDC.put("userId", event.getUserId());

            log.info("\uD83D\uDD10 Received auth event: {} for user {}", event.getActionType(), event.getUserId());

            // Delegate to NotificationService
            notificationService.handleAuthEvent(event);

            if (event.getNotificationType().toString().contains("ERROR") ||
                event.getNotificationType().toString().contains("WARNING")) {
                log.warn("\u26A0\uFE0F Security alert for user {}: {}", event.getUserId(), event.getActionType());
            } else {
                log.info("\u2705 Successfully processed auth event for user {}", event.getUserId());
            }
        } catch (Exception e) {
            log.error("\u274C Error processing auth event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
