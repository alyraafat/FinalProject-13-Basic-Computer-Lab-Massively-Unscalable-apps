package com.redditclone.notification_service.controllers;

import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.models.enums.NotificationPreference;
import com.redditclone.notification_service.services.NotificationService;
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
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("welcome");
    }

    @GetMapping
    public ResponseEntity<List<UserNotification>> getUserNotifications(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
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
    public ResponseEntity<String> readNotification(@RequestHeader(value = "X-User-Id", required = false) UUID userId, @RequestParam String notificationId) {
        notificationService.readNotification(userId, notificationId);
        return ResponseEntity.ok("Read!");
    }

    @PutMapping("/preferences/{preference}")
    public ResponseEntity<String> updatePreferences(@RequestHeader(value = "X-User-Id", required = false) UUID userId,
                                                    @RequestHeader(value = "X-User-Email", required = false) String userHeaderEmail, @PathVariable NotificationPreference preference) {
        try {
            notificationService.updatePreferences(userId, userHeaderEmail, preference);
            return ResponseEntity.ok("Preferences updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Read a single notification by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    /**
     * Update an existing notification’s content.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationRequest request
    ) {
        Notification updated = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a notification.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}