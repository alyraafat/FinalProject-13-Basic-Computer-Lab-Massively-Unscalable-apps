package com.example.miniapp.controllers;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import com.example.miniapp.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotifierFactory notifierFactory;
    private final NotificationRepository notificationRepository;
//    added
    private final NotificationService notificationService;

    public NotificationController(NotifierFactory notifierFactory,
                                  NotificationRepository notificationRepository,
                                  NotificationService notificationService) {
        this.notifierFactory = notifierFactory;
        this.notificationRepository = notificationRepository;
//        added
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserNotification>> getUserNotifications(
            @RequestParam UUID userId,
            @RequestParam(required = false) String status
    ) {
        List<UserNotification> notifications = notificationService.getUserNotifications(userId, status);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest request) {
        notificationService.process(request);
        return ResponseEntity.ok("Sent!");
    }

    @PutMapping
    public ResponseEntity<String> readNotification(@RequestBody UUID userNotificationId) {
        notificationService.readNotification(userNotificationId);
        return ResponseEntity.ok("Read!");
    }



}