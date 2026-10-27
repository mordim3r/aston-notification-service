package com.example.astonnotificationservice;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class UserEventValidator {

    public boolean hasType(JsonNode node) {
        String type = node.path("type").asText(null);
        return type != null && !type.isBlank();
    }

    public String findEmail(JsonNode node) {
        String email = node.path("email").asText(null);
        if (email == null && node.hasNonNull("user")) {
            email = node.path("user").path("email").asText(null);
        }
        if (email == null || email.isBlank()) {
            return null;
        }
        return email;
    }
}