package com.edu.notification_service.listener;

import com.edu.notification_service.dto.CourseEvent;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import jakarta.validation.Valid;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class CourseEventListener {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "course-events-topic", groupId = "notification-group")
    public void handleCourseEvent(@Valid CourseEvent event) {
        try {
            MDC.put("eventType", event.getEventType());
            MDC.put("userId", event.getUserId());
            MDC.put("courseId", event.getCourseId());

            log.info("📚 Received course event: {} for course {} and user {}",
                    event.getEventType(), event.getCourseName(), event.getUserId());

            // Create notification from course event
            Notification notification = new Notification();
            notification.setUserId(Long.valueOf(event.getUserId()));
            notification.setType(event.getNotificationType());
            notification.setMessage(event.getMessage());
            notification.setTitle(String.format("Course %s: %s", event.getEventType().toLowerCase(), event.getCourseName()));
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReadFlag(false);

            // Add course metadata
            try {
                String metadata = objectMapper.writeValueAsString(
                    Map.of("courseId", event.getCourseId(),
                          "courseName", event.getCourseName(),
                          "eventType", event.getEventType())
                );
                notification.setMetadataJson(metadata);
            } catch (Exception e) {
                log.warn("Failed to serialize course metadata", e);
            }

            notification = notificationService.saveNotification(notification);

            // Send real-time notification via WebSocket
            String destination = "/topic/notifications/" + event.getUserId();
            messagingTemplate.convertAndSend(destination, notification);

            if (event.getEventType().equals("COURSE_COMPLETED")) {
                log.info("🎓 Course completion notification sent for user {} - Course: {}",
                        event.getUserId(), event.getCourseName());
            } else {
                log.info("✅ Successfully processed course event: {} for course {}",
                        event.getEventType(), event.getCourseName());
            }
        } catch (Exception e) {
            log.error("❌ Error processing course event: {}", e.getMessage(), e);
            throw e; // Rethrow for Kafka retry
        } finally {
            MDC.clear();
        }
    }
}
