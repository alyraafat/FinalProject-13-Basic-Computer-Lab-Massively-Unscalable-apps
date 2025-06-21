package com.redditclone.notification_service.services.Factory;

import com.redditclone.notification_service.clients.UserClient;
import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.repositories.NotificationRepository;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import com.redditclone.notification_service.repositories.UserNotifyRepository;
import com.redditclone.notification_service.services.SendNotificationStrategyService;
import com.redditclone.notification_service.services.strategy.impl.EmailStrategy;
import com.redditclone.notification_service.services.strategy.impl.PushStrategy;

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
