// src/main/java/com/edu/notification_service/service/EventListenerService.java
package com.edu.notification_service.service;

import com.edu.notification_service.event.CourseCompletedEvent;
import com.edu.notification_service.event.PaymentCompletedEvent;
import com.edu.notification_service.event.EnrollmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventListenerService {
    @KafkaListener(topics = "course-completed", groupId = "notification-group")
    public void handleCourseCompleted(CourseCompletedEvent event) {
        log.info("Received CourseCompletedEvent: {}", event);
        // Handle event logic
    }

    @KafkaListener(topics = "payment-completed", groupId = "notification-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent: {}", event);
        // Handle event logic
    }

    @KafkaListener(topics = "enrollment", groupId = "notification-group")
    public void handleEnrollment(EnrollmentEvent event) {
        log.info("Received EnrollmentEvent: {}", event);
        // Handle event logic
    }
}

