package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.PreferenceService;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import com.example.miniapp.services.strategy.StrategySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

// should implement Notifier
@Component
public class UserNotifier implements Notifier {
    @Autowired
    private StrategySelector strategySelector;
    @Autowired
    private UserNotifyRepository userNotifyRepository;
    private UserNotification userNotification;

    @Override
    public void notify(Notification notification) {
        // Create and save user notification first
        UserNotification userNotification = new UserNotification(notification.getRecieversId().get(0), notification);

        userNotifyRepository.save(userNotification);

        try {
            strategySelector.performDelivery(notification);
        } catch (Exception e) {
            System.out.println("Failed to deliver");
            // Handle delivery failure (log, retry, etc.)
            // You might want to update notification status here
        }
    }

}