package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.PreferenceService;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import com.example.miniapp.services.strategy.StrategySelector;
import org.springframework.stereotype.Component;

import java.util.UUID;

// should implement Notifier
@Component
public class UserNotifier implements Notifier {
    private StrategySelector strategySelector;

    @Override
    public void notify(Notification notification) {

        UUID target = notification.getReceiverId();
        strategySelector.selectStrategy(String.valueOf(target)).deliver(notification);
    }

}