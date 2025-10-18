package com.edu.notification_service.listener;

import com.edu.notification_service.dto.AuthEmailRequest;
import com.edu.notification_service.dto.CourseEmailRequest;
import com.edu.notification_service.dto.SubscriptionEmailRequest;
import com.edu.notification_service.service.EmailService;
import org.edunex.courseservice.event.CourseEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailListener {
    private final EmailService emailService;

    @KafkaListener(topics = "course-email-topic", groupId = "email-group")
    public void handleCourseEmail(CourseEmailEvent event) {
        log.info("📚 Received course email event for: {}", event.getTo());

        CourseEmailRequest emailRequest = new CourseEmailRequest(
                event.getTo(),
                event.getCourseName(),
                event.getStudentName(),
        mapCourseNotificationType(event.getNotificationType())
        );

        emailService.sendCourseEmail(emailRequest);
    }

    @KafkaListener(topics = "auth-email-topic", groupId = "email-group")
    public void handleAuthEmail(AuthEmailRequest emailRequest) {
        log.info("🔐 Received auth email request for: {}", emailRequest.getTo());
        emailService.sendAuthEmail(emailRequest);
    }

    @KafkaListener(topics = "subscription-email-topic", groupId = "email-group")
    public void handleSubscriptionEmail(SubscriptionEmailRequest emailRequest) {
        log.info("💳 Received subscription email request for: {}", emailRequest.getTo());
        emailService.sendSubscriptionEmail(emailRequest);
    }

    private String mapCourseNotificationType(String rawType) {
        if (rawType == null) {
            return "ENROLLMENT";
        }
        return switch (rawType.toUpperCase()) {
            case "COURSE_ENROLLMENT", "ENROLLED" -> "ENROLLMENT";
            case "COURSE_COMPLETION", "COMPLETED" -> "COMPLETION";
            case "LESSON_REMINDER", "COURSE_REMINDER", "REMINDER" -> "REMINDER";
            default -> "ENROLLMENT";
        };
    }
}