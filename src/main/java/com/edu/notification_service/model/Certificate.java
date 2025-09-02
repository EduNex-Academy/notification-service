// src/main/java/com/edu/notification_service/model/Certificate.java
package com.edu.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    private String certificateId;
    private String userId;
    private String courseName;
    private LocalDate issueDate;
}

