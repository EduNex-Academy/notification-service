package com.edu.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class EventDispatcher {
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final CertificateService certificateService;
    private final PaymentSlipService paymentSlipService;

    @Autowired
    public EventDispatcher(NotificationService notificationService, EmailService emailService, CertificateService certificateService, PaymentSlipService paymentSlipService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.certificateService = certificateService;
        this.paymentSlipService = paymentSlipService;
    }

    public void handleEvent(String eventType, Map<String, Object> payload) {
        switch (eventType) {
            case "USER_ENROLLED" -> handleUserEnrolled(payload);
            case "COURSE_COMPLETED" -> handleCourseCompleted(payload);
            case "PAYMENT_CONFIRMED" -> handlePaymentConfirmed(payload);
            default -> {}
        }
    }

    private void handleUserEnrolled(Map<String, Object> payload) {
        // Implement notification, email, etc.
    }

    private void handleCourseCompleted(Map<String, Object> payload) {
        // Implement notification, email, certificate PDF, etc.
    }

    private void handlePaymentConfirmed(Map<String, Object> payload) {
        // Implement notification, email, payment slip PDF, etc.
    }
}

