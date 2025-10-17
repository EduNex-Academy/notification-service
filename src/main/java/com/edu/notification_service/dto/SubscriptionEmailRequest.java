package com.edu.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEmailRequest {
    private String to;
    private String userName;
    private String planName;
    private String planDuration;
    private Double amount;
    private String expiryDate;
    private String notificationType; // SUBSCRIPTION_ACTIVATED, SUBSCRIPTION_CANCELLED, RENEWAL_REMINDER,
                                     // EXPIRY_ALERT, PAYMENT_FAILED
}
