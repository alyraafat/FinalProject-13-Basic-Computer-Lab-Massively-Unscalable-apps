package com.example.miniapp.services.strategy;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StrategySelector {
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private EmailStrategy emailStrategy;

    @Autowired
    private PushStrategy pushStrategy;

    @Autowired
    private UserNotifyRepository userNotifyRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public void performDelivery(UserNotification userNotification) {
//        DeliveryStrategy strategy = selectStrategy(notification.getReceiverId().toString());
        DeliveryStrategy strategy = selectStrategy(userNotification.getUserId(), userNotification);
        Notification notification = notificationRepository.findById(userNotification.getNotificationId()).orElse(null);

        strategy.deliver(userNotification , notification);
    }

    private DeliveryStrategy selectStrategy(UUID userId, UserNotification userNotification) {
        UserPreference preference = preferenceRepository.findById(userId)
                .orElse(new UserPreference(userId));

        preferenceRepository.save(preference); // ensure saved

        switch (preference.getPreference()) {
            case PUSH:
                return pushStrategy;
            case EMAIL:
                userNotification.setStatus("mail");
                userNotifyRepository.save(userNotification);
                return emailStrategy;
            default:
                throw new RuntimeException("No valid delivery method enabled for user");
        }
    }

}

