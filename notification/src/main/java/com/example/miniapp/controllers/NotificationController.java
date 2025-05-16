package com.example.miniapp.controllers;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.dto.PreferenceUpdateRequest;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.enums.NotificationPreference;
import com.example.miniapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint()
    {
        return ResponseEntity.ok("welcome");
    }

    @GetMapping("/")
    public ResponseEntity<List<UserNotification>> getUserNotifications(
            @RequestParam UUID userId,
            @RequestParam(required = false) String status
    ) {
        List<UserNotification> notifications = notificationService.getUserNotifications(userId, status);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/")
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest request) {
        notificationService.process(request);
        return ResponseEntity.ok("Sent!");
    }

    @PutMapping("/read")
    public ResponseEntity<String> readNotification(@RequestBody UUID userId, @RequestParam String notificationId) {
        notificationService.readNotification(userId, notificationId);
        return ResponseEntity.ok("Read!");
    }

    @PutMapping("/preferences/{preference}")
    public ResponseEntity<String> updatePreferences(@RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @RequestHeader(value = "X-User-Email", required = false) String userHeaderEmail,@PathVariable NotificationPreference preference ) {
        try {
            notificationService.updatePreferences(userId, userHeaderEmail, preference);
            return ResponseEntity.ok("Preferences updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}