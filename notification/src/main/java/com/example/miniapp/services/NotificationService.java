package com.example.miniapp.services;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.models.entity.Notification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class NotificationService {
    private final NotifierFactory notifierFactory;
    private final NotificationRepository notificationRepository;
    private final UserNotifyRepository userNotificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(
            NotifierFactory notifierFactory,
            NotificationRepository notificationRepository,
            UserNotifyRepository userNotificationRepository
    ) {
        this.notifierFactory = notifierFactory;
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
    }

    public List<UserNotification> getUserNotifications(UUID userId, String status) {
        if (status == null || status.isBlank()) {
            return userNotificationRepository.findByUserId(userId);
        } else {
            return userNotificationRepository.findByUserIdAndStatus(userId, status.toLowerCase());
        }
    }


    public void process(NotificationRequest request) {

        String sType = request.getType();
        NotificationType type = NotificationType.fromString(sType);
        Notification notification = mapRequestToEntity(request);
        notificationRepository.save(notification);

        Notifier notifier = notifierFactory.create(type);
        notifier.notify(notification);
    }


    public void readNotification(UUID userId, UUID notificationId) {
        Optional<UserNotification> optional = userNotificationRepository.findByUserIdAndNotificationId(userId, notificationId);
        if (optional.isPresent()) {
            UserNotification userNotification = optional.get();
            userNotification.setStatus("read");
            userNotification.setReadAt(Instant.now());
            userNotificationRepository.save(userNotification);
            System.out.println("Notification read successfully");
        } else {
            System.out.println("Failed to mark notification as read: not found for userId = " + userId + " and notificationId = " + notificationId);
        }
    }


//    HELPERS
    private Notification mapRequestToEntity(NotificationRequest request) {
        Notification notification = new Notification();

        // Core fields
        notification.setType(request.getType());
        notification.setMessage(request.getRawMessage());
        notification.setSenderName(request.getSenderName());
        notification.setCreatedAt(Instant.now());
        notification.setReceiversId(request.getReceiversId());
        // Additional fields you might want to set
        notification.setTitle(generateTitleFromType(NotificationType.fromString(request.getType())));
        notification.setSenderId(String.valueOf(request.getSenderId()));

        return notification;
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