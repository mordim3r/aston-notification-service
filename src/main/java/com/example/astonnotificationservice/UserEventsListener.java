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
    private final UserEventValidator validator;

    public UserEventsListener(ObjectMapper objectMapper, EmailService emailService, UserEventValidator validator) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.validator = validator;
    }

    @KafkaListener(topics = "user-events", groupId = "user-events-group")
    public void onUserEvent(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);

            if (!validator.hasType(node)) {
                log.warn("В событии отсутствует поле 'type': {}", message);
                return;
            }
            String text = node.path("type").asText();

            String email = validator.findEmail(node);
            if (email == null) {
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