// src/main/java/com/edu/notification_service/service/NotificationService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.repository.NotificationRepository;
import com.edu.notification_service.model.EmailDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate webSocketTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                             EmailService emailService,
                             SimpMessagingTemplate webSocketTemplate) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.webSocketTemplate = webSocketTemplate;
    }

    public Notification processNotification(NotificationRequest request) {
        logger.info("Processing notification of type: {}", request.getType());

        Notification notification = createNotification(request);

        // Handle notification based on type
        switch (request.getType()) {
            case EMAIL:
                sendEmailNotification(request);
                break;
            case PUSH:
                sendPushNotification(request);
                break;
            default:
                logger.warn("Unsupported notification type: {}", request.getType());
        }

        return notificationRepository.save(notification);
    }

    public void processEmailNotification(NotificationRequest request) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(request.getRecipient());
        emailDetails.setSubject(request.getTitle());
        emailDetails.setBody(request.getMessage());
        emailService.sendEmail(emailDetails);

        // Save notification record
        Notification notification = createNotification(request);
        notificationRepository.save(notification);
    }

    private void sendPushNotification(NotificationRequest request) {
        String destination = "/topic/notifications/" + request.getUserId();
        webSocketTemplate.convertAndSend(destination, request);
        logger.info("Push notification sent to {}", destination);
    }

    private void sendEmailNotification(NotificationRequest request) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(request.getRecipient());
        emailDetails.setSubject(request.getTitle());
        emailDetails.setBody(request.getMessage());
        emailService.sendEmail(emailDetails);
    }

    private Notification createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRecipient(request.getRecipient());
        notification.setMetadataJson(request.getMetadata());
        notification.setReadFlag(false);
        return notification;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
