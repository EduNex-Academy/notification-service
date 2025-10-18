// src/main/java/com/edu/notification_service/event/PaymentCompletedEvent.java
package com.edu.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private String userId;
    private String paymentId;
    private BigDecimal amount;
    private String paymentDate;
}

