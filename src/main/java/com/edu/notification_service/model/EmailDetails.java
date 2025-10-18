package com.edu.notification_service.model;

import lombok.Data;

@Data
public class EmailDetails {
    private String recipient;
    private String subject;
    private String body;
    private String attachment;  // Optional: path to attachment file
}
