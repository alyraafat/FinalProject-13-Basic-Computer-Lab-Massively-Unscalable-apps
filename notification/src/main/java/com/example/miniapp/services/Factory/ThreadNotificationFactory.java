package com.example.miniapp.services.Factory;

import com.example.miniapp.clients.UserClient;
import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.models.enums.NotificationPreference;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.impl.ThreadNotifier;
import com.example.miniapp.services.Factory.impl.UserNotifier;
import com.example.miniapp.services.SendNotificationStrategyService;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ThreadNotificationFactory is responsible for creating instances of Notification for thread-related activities.
 * It uses the NotificationRequest object to determine the type and other parameters needed to create the appropriate Notification instance.
 */
public class ThreadNotificationFactory extends NotificationFactory {

    public ThreadNotificationFactory(UserNotifyRepository userNotificationRepository, SendNotificationStrategyService notifier, PreferenceRepository preferenceRepository, UserClient userClient, EmailStrategy emailStrategy, PushStrategy pushStrategy, NotificationRepository notificationRepository) {
        super(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy, notificationRepository);
    }

    public Notification create(NotificationRequest request) {
        String title = "New Thread Activity";
        return new ThreadNotifier(request.getSenderId().toString(), title, request.getRawMessage(), request.getSenderName(), request.getReceiversId());
    }

    private void notifyThreadOwner(ThreadNotifier notification) {
        UUID receiverId = notification.getThreadOwnerId();
        List<String> emails = userClient.getEmailsByIds(List.of(receiverId));
        String email = emails.getFirst();
        Optional<UserPreference> optional = preferenceRepository.findByUserId(receiverId);
        UserPreference pref = optional.orElse(null);
        if (optional.isEmpty()){
            pref = new UserPreference(receiverId, email);
            preferenceRepository.save(pref);
        }
        notifier.setDeliveryStrategy(pref.getPreference() == NotificationPreference.PUSH ? pushStrategy : emailStrategy);
        String statusString = pref.getPreference() == NotificationPreference.PUSH ? "unread" : "email";
        UserNotification userNotification = new UserNotification(notification, receiverId, statusString, notification.getThreadOwnerMessage());
        userNotificationRepository.save(userNotification);
        notifier.deliver(userNotification);
    }

    public Notification notify(NotificationRequest request) {
        Notification notification = create(request);
        notificationRepository.save(notification);
        ThreadNotifier threadNotifier = (ThreadNotifier) notification;
        // Notify the thread owner
        notifyThreadOwner(threadNotifier);
        List<UUID> receiversId = threadNotifier.getReceiversId();
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
            UserNotification userNotification = new UserNotification(threadNotifier, receiverId, statusString, threadNotifier.getMessage());
            userNotificationRepository.save(userNotification);
            notifier.deliver(userNotification);
        }
        return notification;
    }

}