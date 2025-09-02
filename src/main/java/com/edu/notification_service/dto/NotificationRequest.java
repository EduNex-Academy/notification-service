// src/main/java/com/edu/notification_service/dto/NotificationRequest.java
package com.edu.notification_service.dto;

import com.edu.notification_service.domain.NotificationType;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class NotificationRequest {
    @NotBlank
    private String recipient;
    @NotBlank
    private String message;
    @NotNull
    private NotificationType type;
}
