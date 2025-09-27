package com.edu.notification_service.controller;

import com.edu.notification_service.service.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    private final EventDispatcher eventDispatcher;

    @Autowired
    public WebhookController(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/user-enrolled")
    public ResponseEntity<?> userEnrolled(@RequestBody Map<String, Object> payload) {
        eventDispatcher.handleEvent("USER_ENROLLED", payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/course-completed")
    public ResponseEntity<?> courseCompleted(@RequestBody Map<String, Object> payload) {
        eventDispatcher.handleEvent("COURSE_COMPLETED", payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payment-confirmed")
    public ResponseEntity<?> paymentConfirmed(@RequestBody Map<String, Object> payload) {
        eventDispatcher.handleEvent("PAYMENT_CONFIRMED", payload);
        return ResponseEntity.ok().build();
    }
}

