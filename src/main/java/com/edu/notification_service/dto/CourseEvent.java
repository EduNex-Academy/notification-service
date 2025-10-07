package com.edu.notification_service.dto;

import com.edu.notification_service.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEvent {
    private String userId;
    private String courseId;
    private String courseName;
    private String eventType;
    private String message;
    private NotificationType notificationType;
}
