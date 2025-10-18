package com.edu.notification_service.dto;

import com.edu.notification_service.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthEvent {
    private String userId;
    private String email;
    private String actionType;
    private String message;
    private NotificationType notificationType;
}
