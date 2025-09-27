// src/main/java/com/edu/notification_service/service/NotificationService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setRecipient(request.getRecipient());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setTimestamp(java.time.LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}

