// E:/notification-service/src/test/java/com/edu/notification_service/controller/EmailControllerTest.java
package com.edu.notification_service.controller;

import com.edu.notification_service.dto.EmailRequest;
import com.edu.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(EmailController.class)
class EmailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    void sendEmail_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(emailService).sendEmail(any(EmailRequest.class));
        String json = "{\"to\":\"to@test.com\",\"subject\":\"Subject\",\"body\":\"Body\"}";
        mockMvc.perform(post("/api/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully"));
    }

    @Test
    void sendEmailWithAttachment_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(emailService).sendEmailWithAttachment(any(EmailRequest.class));
        String json = "{\"to\":\"to@test.com\",\"subject\":\"Subject\",\"body\":\"Body\",\"attachment\":\"test.txt\"}";
        mockMvc.perform(post("/api/emails/sendWithAttachment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully with attachment"));
    }
}
