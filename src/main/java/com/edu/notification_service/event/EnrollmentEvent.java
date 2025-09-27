// src/main/java/com/edu/notification_service/event/EnrollmentEvent.java
package com.edu.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentEvent {
    private String userId;
    private String courseId;
    private String enrollmentDate;
}

