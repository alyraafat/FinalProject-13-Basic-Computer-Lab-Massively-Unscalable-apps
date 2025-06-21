package com.redditclone.notification_service.services.Factory;

import com.redditclone.notification_service.clients.UserClient;
import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.models.entity.UserPreference;
import com.redditclone.notification_service.models.enums.NotificationPreference;
import com.redditclone.notification_service.repositories.NotificationRepository;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import com.redditclone.notification_service.repositories.UserNotifyRepository;
import com.redditclone.notification_service.services.Factory.impl.CommunityNotifier;
import com.redditclone.notification_service.services.SendNotificationStrategyService;
import com.redditclone.notification_service.services.strategy.impl.EmailStrategy;
import com.redditclone.notification_service.services.strategy.impl.PushStrategy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CommunityNotificationFactory is responsible for creating instances of Notification for community-related activities.
 * It uses the NotificationRequest object to determine the type and other parameters needed to create the appropriate Notification instance.
 */
public class CommunityNotificationFactory extends NotificationFactory {

    public CommunityNotificationFactory(UserNotifyRepository userNotificationRepository, SendNotificationStrategyService notifier, PreferenceRepository preferenceRepository, UserClient userClient, EmailStrategy emailStrategy, PushStrategy pushStrategy, NotificationRepository notificationRepository) {
        super(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
    }

    public Notification create(NotificationRequest request) {
        String title = "Community Update";
        return new CommunityNotifier(request.getSenderId().toString(), title, request.getRawMessage(), request.getSenderName(), request.getReceiversId());
    }

    public Notification notify(NotificationRequest request) {
        Notification notification = create(request);
        notificationRepository.save(notification);
        List<UUID> receiversId = notification.getReceiversId();
        List<String> emails = userClient.getEmailsByIds(receiversId);
        for (int i = 0; i < receiversId.size(); i++) {
            UUID receiverId = receiversId.get(i);
            String email = emails.get(i);
            Optional<UserPreference> optional = preferenceRepository.findByUserId(receiverId);
            UserPreference pref = optional.orElse(null);
            if (optional.isEmpty()) {
                pref = new UserPreference(receiverId, email);
                preferenceRepository.save(pref);
            }
            notifier.setDeliveryStrategy(pref.getPreference() == NotificationPreference.PUSH ? pushStrategy : emailStrategy);
            String statusString = pref.getPreference() == NotificationPreference.PUSH ? "unread" : "email";
            UserNotification userNotification = new UserNotification(notification, receiverId, statusString, notification.getMessage());
            userNotificationRepository.save(userNotification);
            notifier.deliver(userNotification);
        }
        return notification;
    }
}