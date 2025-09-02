// src/main/java/com/edu/notification_service/model/EmailDetails.java
package com.edu.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    private String to;
    private String subject;
    private String body;
    private String attachment; // Optional: file path or base64
}

