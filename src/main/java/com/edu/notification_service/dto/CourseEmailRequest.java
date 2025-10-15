package com.edu.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEmailRequest {
    private String to;
    private String courseName;
    private String studentName;
    private String notificationType; // ENROLLMENT, COMPLETION, REMINDER
}
