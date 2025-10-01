// src/main/java/com/edu/notification_service/service/NotificationService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.domain.NotificationType;
import com.edu.notification_service.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate webSocketTemplate;

    public Notification sendNotification(@Valid NotificationRequest request) {
        logger.info("Processing notification request for user: {}, type: {}", request.getUserId(), request.getType());

        // Create and save notification
        Notification notification = createNotification(request);
        notification = notificationRepository.save(notification);
        logger.info("Notification saved with ID: {}", notification.getId());

        // Send push notification if type is PUSH
        if (request.getType() == NotificationType.COURSE_ENROLLMENT
                || request.getType() == NotificationType.PAYMENT_SUCCESS
                || request.getType() == NotificationType.SUBSCRIPTION_RENEWAL) {
            sendPushNotification(notification);
        }

        return notification;
    }

    private void sendPushNotification(Notification notification) {
        String destination = "/topic/notifications/" + notification.getUserId();
        webSocketTemplate.convertAndSend(destination, notification);
        logger.info("Push notification sent to {} for user: {}", destination, notification.getUserId());
    }

    private Notification createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRecipient(request.getRecipient());
        notification.setMetadataJson(request.getMetadata());
        notification.setReadFlag(false);
        return notification;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
