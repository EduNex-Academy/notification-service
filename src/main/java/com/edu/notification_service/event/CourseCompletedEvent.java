// src/main/java/com/edu/notification_service/event/CourseCompletedEvent.java
package com.edu.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCompletedEvent {
    private String userId;
    private String courseId;
    private String courseName;
    private String completionDate;
}

