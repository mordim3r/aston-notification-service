package com.example.astonnotificationservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventsListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventsListener.class);

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public UserEventsListener(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "user-events-group")
    public void onUserEvent(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);

            String text = node.path("type").asText(null);
            if (text == null || text.isBlank()) {
                log.warn("В событии отсутствует поле 'type': {}", message);
                return;
            }
            String email = node.path("email").asText(null);
            if (email == null && node.hasNonNull("user")) {
                email = node.path("user").path("email").asText(null);
            }

            if (email == null || email.isBlank()) {
                log.warn("Email не найден в событии: {}", message);
                return;
            }

            emailService.send(email, "Уведомление", text);
            log.info("Email успешно отправлен на {}: {}", email, text);

        } catch (Exception e) {
            log.error("Ошибка обработки сообщения из Kafka: {}", message, e);
        }
    }
}