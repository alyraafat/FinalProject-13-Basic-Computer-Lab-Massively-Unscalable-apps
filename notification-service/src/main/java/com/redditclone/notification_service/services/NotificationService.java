package com.redditclone.notification_service.services;

import com.redditclone.notification_service.clients.UserClient;
import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.models.entity.UserPreference;
import com.redditclone.notification_service.models.enums.NotificationPreference;
import com.redditclone.notification_service.models.enums.NotificationType;
import com.redditclone.notification_service.repositories.NotificationRepository;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import com.redditclone.notification_service.repositories.UserNotifyRepository;
import com.redditclone.notification_service.services.Factory.CommunityNotificationFactory;
import com.redditclone.notification_service.services.Factory.NotificationFactory;
import com.redditclone.notification_service.services.Factory.ThreadNotificationFactory;
import com.redditclone.notification_service.services.Factory.UserNotificationFactory;
import com.redditclone.notification_service.services.strategy.impl.EmailStrategy;
import com.redditclone.notification_service.services.strategy.impl.PushStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotifyRepository userNotificationRepository;
    private final SendNotificationStrategyService notifier;
    private final PreferenceRepository preferenceRepository;
    private final UserClient userClient;
    private final EmailStrategy emailStrategy;
    private final PushStrategy pushStrategy;
    private NotificationFactory notificationFactory;

    @Autowired
    public NotificationService(
            NotificationRepository notificationRepository,
            UserNotifyRepository userNotificationRepository,
            SendNotificationStrategyService notifier,
            PreferenceRepository preferenceRepository,
            UserClient userClient,
            EmailStrategy emailStrategy,
            PushStrategy pushStrategy
    ) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.notifier = notifier;
        this.preferenceRepository = preferenceRepository;
        this.userClient = userClient;
        this.emailStrategy = emailStrategy;
        this.pushStrategy = pushStrategy;
    }

    public List<UserNotification> getUserNotifications(UUID userId, String status) {
        if (status == null || status.isBlank()) {
            return userNotificationRepository.findByUserId(userId);
        } else {
            return userNotificationRepository.findByUserIdAndStatus(userId, status.toLowerCase());
        }
    }

    private void createNotificationFactory(NotificationType type) {
        switch (type) {
            case USER_SPECIFIC ->
                    notificationFactory = new UserNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
            case THREAD ->
                    notificationFactory = new ThreadNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
            case COMMUNITY ->
                    notificationFactory = new CommunityNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
        }
    }

    public void process(NotificationRequest request) {
        createNotificationFactory(request.getType());
        Notification notification = notificationFactory.notify(request);

    }


    public void readNotification(UUID userId, String notificationId) {
        Optional<UserNotification> optional = userNotificationRepository.findByUserIdAndNotification_Id(userId, notificationId);
//        List<UserNotification> userNotifications = userNotificationRepository.findByUserIdAndStatus(userId, "read");
        if (optional.isPresent()) {
//        if (!userNotifications.isEmpty()) {
            UserNotification userNotification = optional.get();
//            UserNotification userNotification = userNotifications.getFirst();
            userNotification.setStatus("read");
            userNotification.setReadAt(Instant.now());
            userNotificationRepository.save(userNotification);
            System.out.println("Notification read successfully");
        } else {
            System.out.println("Failed to mark notification as read: not found for userId = " + userId + " and notificationId = " + notificationId);
        }
    }

    public void updatePreferences(UUID userId, String email, NotificationPreference preference) {
        UserPreference pref = preferenceRepository.findByUserId(userId)
                .orElse(new UserPreference(userId, email));
        pref.setUserEmail(email);
        pref.setPreference(preference);
        preferenceRepository.save(pref);
    }


    public Notification getNotificationById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));
    }

    public Notification updateNotification(String id, NotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));
        notification.setMessage(request.getRawMessage());
        notification.setType(request.getType());
        notification.setReceiversId(request.getReceiversId());
        notification.setSenderId(String.valueOf(request.getSenderId()));
        notification.setSenderName(request.getSenderName());
        return notificationRepository.save(notification);

    }

    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }
}