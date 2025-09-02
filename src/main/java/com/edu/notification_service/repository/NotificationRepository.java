// src/main/java/com/edu/notification_service/repository/NotificationRepository.java
package com.edu.notification_service.repository;

import com.edu.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

