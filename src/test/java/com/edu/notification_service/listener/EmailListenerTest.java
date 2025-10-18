package com.edu.notification_service.listener;

import com.edu.notification_service.dto.CourseEmailRequest;
import com.edu.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

/**
 * Unit test to verify CourseEmailRequest events are consumed by EmailListener
 * and delegated to EmailService. This test calls the listener method directly
 * (simulating a consumed Kafka message) and verifies EmailService is invoked.
 */
@ExtendWith(MockitoExtension.class)
class EmailListenerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailListener emailListener;

    @Test
    void testHandleCourseEmail_callsEmailService() {
        // Arrange: create a CourseEmailRequest using the DTO fields
        CourseEmailRequest req = new CourseEmailRequest(
                "student@example.com",
                "Introduction to Java",
                "Jane Student",
                "COMPLETION"
        );

        // Act: invoke the listener method as if a Kafka message was received
        emailListener.handleCourseEmail(req);

        // Assert: ensure EmailService was called to send the course email
        verify(emailService).sendCourseEmail(req);
    }
}
