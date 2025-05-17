package com.example.miniapp.services.Factory;

import com.example.miniapp.clients.UserClient;
import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.models.enums.NotificationPreference;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.SendNotificationStrategyService;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * NotificationFactory is an abstract class that serves as a base for creating different types of notifications.
 * It uses the NotificationRequest object to determine the type and other parameters needed to create the appropriate Notification instance.
 */
public abstract class NotificationFactory {
    private final UserNotifyRepository userNotificationRepository;
    private final SendNotificationStrategyService notifier;
    private final PreferenceRepository preferenceRepository;
    private final UserClient userClient;
    private final EmailStrategy emailStrategy;
    private final PushStrategy pushStrategy;
    private final NotificationRepository notificationRepository;

    public NotificationFactory(UserNotifyRepository userNotificationRepository, SendNotificationStrategyService notifier, PreferenceRepository preferenceRepository, UserClient userClient, EmailStrategy emailStrategy, PushStrategy pushStrategy, NotificationRepository notificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
        this.notifier = notifier;
        this.preferenceRepository = preferenceRepository;
        this.userClient = userClient;
        this.emailStrategy = emailStrategy;
        this.pushStrategy = pushStrategy;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Create the right Notification subtype based on category.
     *
     * @param request the NotificationRequest object containing the details of the notification
     * @return a Notification instance
     */
    public abstract Notification create(NotificationRequest request);

    public Notification notify(NotificationRequest request) {
        Notification notification = create(request);
        notificationRepository.save(notification);
        List<UUID> receiversId = notification.getReceiversId();
        System.out.println(receiversId);
        List<String> emails = userClient.getEmailsByIds(receiversId);
        for (int i = 0; i < receiversId.size(); i++) {
            UUID receiverId = receiversId.get(i);
            String email = emails.get(i);
            Optional<UserPreference> optional = preferenceRepository.findByUserId(receiverId);
            UserPreference pref = optional.orElse(null);
            if (optional.isEmpty()){
                pref = new UserPreference(receiverId, email);
                preferenceRepository.save(pref);
            }
            notifier.setDeliveryStrategy(pref.getPreference() == NotificationPreference.PUSH ? pushStrategy : emailStrategy);
            String statusString = pref.getPreference() == NotificationPreference.PUSH ? "unread" : "email";
            UserNotification userNotification = new UserNotification(notification, receiverId, statusString);
            userNotificationRepository.save(userNotification);
            notifier.deliver(userNotification);
        }
        return notification;
    }
}
