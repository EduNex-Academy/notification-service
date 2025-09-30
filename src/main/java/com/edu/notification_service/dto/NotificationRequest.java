package com.edu.notification_service.dto;

import com.edu.notification_service.domain.NotificationType;
import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String recipient;
    private String title;
    private String message;
    private NotificationType type;
    private String metadata;
    private String attachment; // Optional: for email attachments
}
