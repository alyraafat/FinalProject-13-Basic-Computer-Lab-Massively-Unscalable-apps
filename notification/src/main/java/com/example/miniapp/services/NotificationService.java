package com.example.miniapp.services;

import com.example.miniapp.clients.UserClient;
import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.dto.PreferenceUpdateRequest;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.models.enums.NotificationPreference;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.CommunityNotificationFactory;
import com.example.miniapp.services.Factory.NotificationFactory;
import com.example.miniapp.services.Factory.ThreadNotificationFactory;
import com.example.miniapp.services.Factory.UserNotificationFactory;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.miniapp.models.entity.Notification;

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

    private NotificationFactory createNotificationFactory(NotificationType type){
        return switch (type) {
            case USER_SPECIFIC ->
                    new UserNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy);
            case THREAD ->
                    new ThreadNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy);
            case COMMUNITY ->
                    new CommunityNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy);
        };
    }

    public void process(NotificationRequest request) {
        NotificationFactory notifierFactory = createNotificationFactory(request.getType());
        Notification notification = notifierFactory.notify(request);
        notificationRepository.save(notification);
    }


    public void readNotification(UUID userId, String notificationId) {
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

    public void updatePreferences(String email, PreferenceUpdateRequest request) {
        if (request.getPreference() == null) {
            throw new IllegalArgumentException("Preference must be set.");
        }

        UserPreference pref = preferenceRepository.findByUserId(request.getUserId())
                .orElse(new UserPreference(request.getUserId(), email));

        pref.setPreference(request.getPreference());

        preferenceRepository.save(pref);
    }



}