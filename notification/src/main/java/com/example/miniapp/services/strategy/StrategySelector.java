package com.example.miniapp.services.strategy;

import com.example.miniapp.models.entity.UserPreference;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.stereotype.Service;

@Service
public class StrategySelector {
    private final PreferenceRepository preferenceRepository;
    private final PushStrategy pushStrategy;
    private final EmailStrategy emailStrategy;

    public StrategySelector(PreferenceRepository preferenceRepository,
                            PushStrategy pushStrategy,
                            EmailStrategy emailStrategy) {
        this.preferenceRepository = preferenceRepository;
        this.pushStrategy = pushStrategy;
        this.emailStrategy = emailStrategy;
    }

    public DeliveryStrategy selectStrategy(String userId) {
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