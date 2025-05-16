package com.example.miniapp.services.Factory;

import com.example.miniapp.clients.UserClient;
import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
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

/**
 * ThreadNotificationFactory is responsible for creating instances of Notification for thread-related activities.
 * It uses the NotificationRequest object to determine the type and other parameters needed to create the appropriate Notification instance.
 */
public class ThreadNotificationFactory extends NotificationFactory {

    public ThreadNotificationFactory(UserNotifyRepository userNotificationRepository, SendNotificationStrategyService notifier, PreferenceRepository preferenceRepository, UserClient userClient, EmailStrategy emailStrategy, PushStrategy pushStrategy) {
        super(userNotificationRepository, notifier, preferenceRepository, userClient, emailStrategy, pushStrategy);
    }

    public Notification create(NotificationRequest request) {
        String title = "New Thread Activity";
        return new ThreadNotifier(request.getSenderId().toString(), title, request.getRawMessage(), request.getSenderName(), request.getReceiversId());
    }

}