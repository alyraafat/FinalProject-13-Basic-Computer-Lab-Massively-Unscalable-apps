package com.example.miniapp.services.strategy;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategySelector {
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private EmailStrategy emailStrategy;

    @Autowired
    private PushStrategy pushStrategy;

    public void performDelivery(Notification notification) {
        DeliveryStrategy strategy = selectStrategy(notification.getReceiverId().toString());
        strategy.deliver(notification);
    }

    private DeliveryStrategy selectStrategy(String userId) {
        UserPreference preference = preferenceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User preferences not found"));

        if (preference.isPushNotifications()) {
            return pushStrategy;
        } else if (preference.isEmailNotifications()) {
            return emailStrategy;
        }

        throw new RuntimeException("No delivery method enabled for user");
    }
}

