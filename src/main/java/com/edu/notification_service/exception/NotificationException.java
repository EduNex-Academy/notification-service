// src/main/java/com/edu/notification_service/exception/NotificationException.java
package com.edu.notification_service.exception;

public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}

