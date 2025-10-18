// E:/notification-service/src/test/java/com/edu/notification_service/service/NotificationServiceTest.java
package com.edu.notification_service.service;

import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.domain.NotificationType;
import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_shouldSaveNotification() {
        NotificationRequest request = new NotificationRequest();
        request.setMessage("Test message");
        request.setRecipient("USER");
        request.setType(NotificationType.valueOf("INFO"));
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Notification result = notificationService.sendNotification(request);
        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotifications() {
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification1, notification2));
        List<Notification> result = notificationService.getAllNotifications();
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findAll();
    }
}
