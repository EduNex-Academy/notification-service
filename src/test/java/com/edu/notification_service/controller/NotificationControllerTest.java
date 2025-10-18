// E:/notification-service/src/test/java/com/edu/notification_service/controller/NotificationControllerTest.java
package com.edu.notification_service.controller;

import com.edu.notification_service.domain.Notification;
import com.edu.notification_service.dto.NotificationRequest;
import com.edu.notification_service.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void postNotification_shouldReturnSavedNotification() throws Exception {
        Notification notification = new Notification();
        when(notificationService.sendNotification(any(NotificationRequest.class))).thenReturn(notification);
        String json = "{\"message\":\"Test\",\"recipient\":\"USER\",\"type\":\"INFO\"}";
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotifications() throws Exception {
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        when(notificationService.getAllNotifications()).thenReturn(Arrays.asList(notification1, notification2));
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk());
    }
}

