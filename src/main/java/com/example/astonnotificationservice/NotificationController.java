package com.example.astonnotificationservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void send(@RequestBody SendNotificationRequest request) {
        emailService.send(request.getEmail(), "Уведомление", request.getText());
    }
}