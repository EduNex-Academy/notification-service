// src/main/java/com/edu/notification_service/controller/EmailController.java
package com.edu.notification_service.controller;

import com.edu.notification_service.dto.EmailRequest;
import com.edu.notification_service.model.EmailDetails;
import com.edu.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    /*public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok("Email sent successfully");
    }*/
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
    @PostMapping("/sendWithAttachment")
    public ResponseEntity<String> sendEmailWithAttachment(@RequestBody EmailRequest request) {
        emailService.sendEmailWithAttachment(request);
        return ResponseEntity.ok("Email with attachment sent successfully");
    }
}

