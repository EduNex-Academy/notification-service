package com.edu.notification_service.listener;

import com.edu.notification_service.dto.CourseEvent;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class CourseEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "course-events-topic", groupId = "notification-group")
    public void handleCourseEvent(@Valid CourseEvent event) {
        try {
            MDC.put("eventType", event.getEventType());
            MDC.put("userId", event.getUserId());
            MDC.put("courseId", event.getCourseId());

            log.info("\uD83D\uDCDA Received course event: {} for course {} and user {}",
                    event.getEventType(), event.getCourseName(), event.getUserId());

            // Delegate to NotificationService
            notificationService.handleCourseEvent(event);

            if (event.getNotificationType().toString().contains("ERROR") ||
                event.getNotificationType().toString().contains("WARNING")) {
                log.warn("\u26A0\uFE0F Course alert for user {}: {}", event.getUserId(), event.getEventType());
            } else {
                log.info("\u2705 Successfully processed course event for user {}", event.getUserId());
            }
        } catch (Exception e) {
            log.error("\u274C Error processing course event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
