package com.edu.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthEmailRequest {
    private String to;
    private String username;
    private String verificationLink;
    private String notificationType; // REGISTRATION, PASSWORD_RESET, LOGIN_ALERT
    private String ipAddress;
    private String deviceInfo;
}
