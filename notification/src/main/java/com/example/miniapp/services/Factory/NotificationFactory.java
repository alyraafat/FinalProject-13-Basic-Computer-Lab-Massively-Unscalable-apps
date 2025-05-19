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
    protected final UserNotifyRepository userNotificationRepository;
    protected final SendNotificationStrategyService notifier;
    protected final PreferenceRepository preferenceRepository;
    protected final UserClient userClient;
    protected final EmailStrategy emailStrategy;
    protected final PushStrategy pushStrategy;
    protected final NotificationRepository notificationRepository;

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

    public abstract Notification notify(NotificationRequest request);
}
