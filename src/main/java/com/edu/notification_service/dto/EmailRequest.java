// src/main/java/com/edu/notification_service/dto/EmailRequest.java
package com.edu.notification_service.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailRequest {
    @Email
    @NotBlank
    private String to;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
    private String attachment; // Optional
}

