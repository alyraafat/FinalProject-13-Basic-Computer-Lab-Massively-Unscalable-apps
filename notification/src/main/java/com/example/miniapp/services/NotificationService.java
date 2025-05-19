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

    private void createNotificationFactory(NotificationType type){
        switch (type) {
            case USER_SPECIFIC ->
                    notificationFactory = new UserNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
            case THREAD ->
                    notificationFactory = new ThreadNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
            case COMMUNITY ->
                    notificationFactory = new CommunityNotificationFactory(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
        };
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



}