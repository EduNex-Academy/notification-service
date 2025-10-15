package com.edu.notification_service.controller;

import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public String broadcast(String message) {
        System.out.println("Received message: " + message);
        return "Server received: " + message;
    }
}


