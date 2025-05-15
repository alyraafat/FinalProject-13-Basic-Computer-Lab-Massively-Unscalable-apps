package com.example.miniapp.services;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.dto.PreferenceUpdateRequest;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
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

    @Autowired
    private PreferenceRepository preferenceRepository;

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
        String title = generateTitleFromType(request.getType());
        Notification notification = new Notification(request.getType(), request.getSenderId().toString(), title, request.getRawMessage(), request.getSenderName(), request.getReceiversId());
        notificationRepository.save(notification);
        Notifier notifier = notifierFactory.create(request.getType());
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

    public void updatePreferences(PreferenceUpdateRequest request) {
        if (request.getPreference() == null) {
            throw new IllegalArgumentException("Preference must be set.");
        }

        UserPreference pref = preferenceRepository.findById(request.getUserId())
                .orElse(new UserPreference(request.getUserId()));

        pref.setPreference(request.getPreference());

        if (request.getUserEmail() != null && !request.getUserEmail().isBlank()) {
            pref.setUserEmail(request.getUserEmail());
        }

        preferenceRepository.save(pref);
    }

    private String generateTitleFromType(NotificationType type) {
        switch (type) {
            case COMMUNITY:
                return "Community Update";
            case THREAD:
                return "New Thread Activity";
            case USER_SPECIFIC:
                return "User Notification";
            default:
                return "Notification";
        }
    }


}