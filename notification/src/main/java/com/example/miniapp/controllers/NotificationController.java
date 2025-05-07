package com.example.miniapp.controllers;

import com.example.miniapp.models.dto.NotificationResponse;
import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import com.example.miniapp.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

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

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        // Strategy will also be selected inside the factory
//        Notifier notifier = notifierFactory.createNotifier(request);
//        notifier.send(request);

        // Save to database
//        YOUR PREVIOUS COMMIT
        Notification notification = mapRequestToEntity(request);
//        notificationRepository.save(notification);

        notificationService.process(request);

//        commented
//        return ResponseEntity.ok(mapToResponse(notification));
        return ResponseEntity.ok(new NotificationResponse());
    }
//  PREVIOUS COMMIT
//    private Notification mapRequestToEntity(NotificationRequest request) {
//        Notification notification = new Notification();
//
//        // Core fields
//        notification.setType(request.getType().toString());
//        notification.setMessage(request.getRawMessage());
//        notification.setCreatedAt(Instant.now());
//
//        // Additional fields you might want to set
//        notification.setTitle(generateTitleFromType(request.getType()));
//        notification.setSenderId(request.getActorUserId()); // Or another sender identifier
//
//        return notification;
//    }

    private NotificationResponse mapToResponse(Notification notification) {
//        return NotificationResponse.builder()
//                .id(notification.getId())
//                .type(notification.getType())
//                .title(notification.getTitle())
//                .message(notification.getMessage())
//                .createdAt(notification.getCreatedAt())
//                .status("DELIVERED") // Or get actual status from UserNotification
//                .build();
        return new NotificationResponse();
    }

    private String generateTitleFromType(NotificationType type) {
        switch(type) {
            case COMMUNITY: return "Community Update";
            case THREAD: return "New Thread Activity";
            case USER_SPECIFIC: return "User Notification";
            default: return "Notification";
        }
    }

}