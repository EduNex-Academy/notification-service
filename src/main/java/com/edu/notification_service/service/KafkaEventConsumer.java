package com.edu.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class KafkaEventConsumer {
    private final EventDispatcher eventDispatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KafkaEventConsumer(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @KafkaListener(topics = {"user.events", "course.events", "payment.events"}, groupId = "notification-service")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> event = objectMapper.readValue(record.value(), Map.class);
            String eventType = (String) event.get("type");
            Map<String, Object> payload = (Map<String, Object>) event.get("payload");
            eventDispatcher.handleEvent(eventType, payload);
        } catch (Exception e) {
            // Log error
            e.printStackTrace();
        }
    }
}

