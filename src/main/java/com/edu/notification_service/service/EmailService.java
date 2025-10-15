// src/main/java/com/edu/notification_service/service/EmailService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.AuthEmailRequest;
import com.edu.notification_service.dto.CourseEmailRequest;
import com.edu.notification_service.dto.EmailRequest;
import com.edu.notification_service.dto.SubscriptionEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendCourseEmail(CourseEmailRequest request) {
        try {
            Context context = new Context();
            context.setVariable("studentName", request.getStudentName());
            context.setVariable("courseName", request.getCourseName());

            String template = switch (request.getNotificationType().toUpperCase()) {
                case "ENROLLMENT" -> "emails/course-enrollment.html";
                case "COMPLETION" -> "emails/course-completion.html";
                case "REMINDER" -> "emails/course-reminder.html";
                default -> throw new IllegalArgumentException("Invalid course notification type");
            };

            String subject = switch (request.getNotificationType().toUpperCase()) {
                case "ENROLLMENT" -> "Welcome to " + request.getCourseName();
                case "COMPLETION" -> "Congratulations on Completing " + request.getCourseName();
                case "REMINDER" -> "Course Reminder: " + request.getCourseName();
                default -> "Course Notification";
            };

            sendEmail(request.getTo(), subject, template, context);
            log.info("📚 Course email sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("❌ Failed to send course email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send course email", e);
        }
    }

    public void sendAuthEmail(AuthEmailRequest request) {
        try {
            Context context = new Context();
            context.setVariable("username", request.getUsername());
            context.setVariable("verificationLink", request.getVerificationLink());
            context.setVariable("ipAddress", request.getIpAddress());
            context.setVariable("deviceInfo", request.getDeviceInfo());

            String template = switch (request.getNotificationType().toUpperCase()) {
                case "REGISTRATION" -> "emails/registration-verification.html";
                case "PASSWORD_RESET" -> "emails/password-reset.html";
                case "LOGIN_ALERT" -> "emails/login-alert.html";
                default -> throw new IllegalArgumentException("Invalid auth notification type");
            };

            String subject = switch (request.getNotificationType().toUpperCase()) {
                case "REGISTRATION" -> "Verify Your Email Address";
                case "PASSWORD_RESET" -> "Password Reset Request";
                case "LOGIN_ALERT" -> "New Login Alert";
                default -> "Authentication Notification";
            };

            sendEmail(request.getTo(), subject, template, context);
            log.info("🔐 Auth email sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("❌ Failed to send auth email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send auth email", e);
        }
    }

    public void sendSubscriptionEmail(SubscriptionEmailRequest request) {
        try {
            Context context = new Context();
            context.setVariable("userName", request.getUserName());
            context.setVariable("planName", request.getPlanName());
            context.setVariable("planDuration", request.getPlanDuration());
            context.setVariable("amount", request.getAmount());
            context.setVariable("expiryDate", request.getExpiryDate());

            String template = switch (request.getNotificationType().toUpperCase()) {
                case "SUBSCRIPTION_ACTIVATED" -> "emails/subscription-activated.html";
                case "RENEWAL_REMINDER" -> "emails/subscription-renewal-reminder.html";
                case "EXPIRY_ALERT" -> "emails/subscription-expiry-alert.html";
                case "PAYMENT_FAILED" -> "emails/subscription-payment-failed.html";
                default -> throw new IllegalArgumentException("Invalid subscription notification type");
            };

            String subject = switch (request.getNotificationType().toUpperCase()) {
                case "SUBSCRIPTION_ACTIVATED" -> "Subscription Activated Successfully";
                case "RENEWAL_REMINDER" -> "Subscription Renewal Reminder";
                case "EXPIRY_ALERT" -> "Subscription Expiry Alert";
                case "PAYMENT_FAILED" -> "Subscription Payment Failed";
                default -> "Subscription Notification";
            };

            sendEmail(request.getTo(), subject, template, context);
            log.info("💳 Subscription email sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("❌ Failed to send subscription email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send subscription email", e);
        }
    }

    private void sendEmail(String to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = templateEngine.process(templateName, context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
