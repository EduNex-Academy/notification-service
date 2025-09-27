// src/main/java/com/edu/notification_service/service/PaymentSlipService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.PaymentSlipRequest;
import com.edu.notification_service.model.PaymentSlip;
import org.springframework.stereotype.Service;

@Service
public class PaymentSlipService {
    public PaymentSlip generatePaymentSlip(PaymentSlipRequest request) {
        PaymentSlip slip = new PaymentSlip();
        slip.setUserId(request.getUserId());
        slip.setAmount(request.getAmount());
        slip.setDueDate(request.getDueDate());
        slip.setSlipId(java.util.UUID.randomUUID().toString());
        return slip;
    }
}
