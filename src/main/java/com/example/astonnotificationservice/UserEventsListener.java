package com.example.astonnotificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventsListener {
    @KafkaListener(topics = "user-events", groupId = "user-events-group")
    public void onUserEvent(String message){
        System.out.println("Received: "+ message);
    }

}
