# Notification Service

A Spring Boot 3 (Java 21) microservice for handling notifications, HTML emails, PDF generation, and real-time in-app notifications.

## Features
- Listens to events from other microservices (Kafka or webhooks)
- Persists notifications
- Sends HTML emails (Thymeleaf)
- Generates PDFs (certificates, payment slips)
- Pushes real-time notifications via WebSocket/STOMP

## Prerequisites
- Docker & Docker Compose
- Java 21

## Running the Stack

1. **Start dependencies:**
   ```sh
   docker-compose up
   ```
   This starts Zookeeper (2181), Kafka (9092), MailHog (1025/8025), and the notification-service (8080).

2. **Run the app locally (if not using the container):**
   ```sh
   ./gradlew bootRun
   ```

## Testing

- **Send a mock event via Kafka console producer:**
  ```sh
  docker exec -it <kafka-container-name> kafka-console-producer --broker-list localhost:9092 --topic user.events
  > {"type":"USER_ENROLLED","payload":{"userId":1,"userName":"Alice"}}
  ```

- **Check email in MailHog:**
  Visit [http://localhost:8025](http://localhost:8025)

## API Endpoints
- `GET /api/notifications?userId=...` — List notifications
- `PUT /api/notifications/{id}/read` — Mark as read
- `POST /api/webhooks/user-enrolled` — Fallback event
- WebSocket endpoint: `/ws` (STOMP, broker `/topic`)

## PDF Output
- Generated PDFs are saved in `./generated-pdfs`

---

**Note:**
- The notification-service container is optional; you can run the app locally for development.
- Update the Dockerfile as needed for your build process.

