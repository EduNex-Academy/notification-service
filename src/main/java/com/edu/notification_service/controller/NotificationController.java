// src/main/java/com/edu/notification_service/controller/NotificationController.java
package com.edu.notification_service.controller;

import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest request) {
        Notification notification = notificationService.sendNotification(request);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
