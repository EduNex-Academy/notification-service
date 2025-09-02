// src/main/java/com/edu/notification_service/dto/PaymentSlipRequest.java
package com.edu.notification_service.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentSlipRequest {
    @NotBlank
    private String userId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate dueDate;
}

