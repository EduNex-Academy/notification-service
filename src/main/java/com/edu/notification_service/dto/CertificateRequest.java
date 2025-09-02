// src/main/java/com/edu/notification_service/dto/CertificateRequest.java
package com.edu.notification_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CertificateRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String courseName;
}
