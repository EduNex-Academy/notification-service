// src/main/java/com/edu/notification_service/service/NotificationService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.SubscriptionEvent;
import com.edu.notification_service.dto.AuthEvent;
import com.edu.notification_service.dto.CourseEvent;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate webSocketTemplate;

    // Handler for subscription events
    public void handleSubscriptionEvent(SubscriptionEvent event) {
        logger.info("Handling subscription event: {} for user: {}", event.getEventType(), event.getUserId());
        String message = event.getMessage();
        sendPushNotification(event.getUserId(), message);
    }

    // Handler for authentication events
    public void handleAuthEvent(AuthEvent event) {
        logger.info("Handling auth event: {} for user: {}", event.getActionType(), event.getUserId());
        String message = event.getMessage();
        sendPushNotification(event.getUserId(), message);
    }

    // Handler for course events
    public void handleCourseEvent(CourseEvent event) {
        logger.info("Handling course event: {} for user: {}", event.getEventType(), event.getUserId());
        String message = event.getMessage();
        sendPushNotification(event.getUserId(), message);
    }

    // Common push notification logic
    private void sendPushNotification(String userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(Long.valueOf(userId));
        notification.setMessage(message);
        notification.setReadFlag(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        String destination = "/topic/notifications/" + userId;
        webSocketTemplate.convertAndSend(destination, notification);
        logger.info("Push notification sent to {} for user: {}", destination, userId);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification saveNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }
}
