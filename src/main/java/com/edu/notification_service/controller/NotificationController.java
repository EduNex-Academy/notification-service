// src/main/java/com/edu/notification_service/controller/NotificationController.java
package com.edu.notification_service.controller;

import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @deprecated This controller only provides GET endpoints for retrieving notifications.
 * For sending notifications, use Kafka messages to 'push-notification-topic'.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
