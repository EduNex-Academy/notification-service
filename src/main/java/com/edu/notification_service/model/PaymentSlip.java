// src/main/java/com/edu/notification_service/model/PaymentSlip.java
package com.edu.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSlip {
    private String slipId;
    private String userId;
    private BigDecimal amount;
    private LocalDate dueDate;
}

