package com.edu.notification_service.listener;

import org.edunex.courseservice.event.CourseEmailEvent;
import com.edu.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class CourseEmailListener {

    private final EmailService emailService;

    @KafkaListener(topics = "course-email-topic", groupId = "notification-group")
    public void handleCourseEmailEvent(@Valid CourseEmailEvent event) {
        try {
            MDC.put("studentName", event.getStudentName());
            MDC.put("to", event.getTo());

            log.info("📧 Received course email notification for course {} and student {}", 
                    event.getCourseName(), event.getStudentName());

            Map<String, Object> templateVariables = new HashMap<>();
            templateVariables.put("courseName", event.getCourseName());
            templateVariables.put("studentName", event.getStudentName());
            
            // Convert the map to a Context
            org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
            templateVariables.forEach(context::setVariable);
            
            // Send course enrollment email using the private method through a CourseEmailRequest
            com.edu.notification_service.dto.CourseEmailRequest emailRequest = new com.edu.notification_service.dto.CourseEmailRequest();
            emailRequest.setTo(event.getTo());
            emailRequest.setStudentName(event.getStudentName());
            emailRequest.setCourseName(event.getCourseName());
            emailRequest.setNotificationType("ENROLLMENT");
            
            emailService.sendCourseEmail(emailRequest);

            log.info("✅ Successfully sent enrollment email to {}", event.getTo());
        } catch (Exception e) {
            log.error("❌ Error sending enrollment email: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}