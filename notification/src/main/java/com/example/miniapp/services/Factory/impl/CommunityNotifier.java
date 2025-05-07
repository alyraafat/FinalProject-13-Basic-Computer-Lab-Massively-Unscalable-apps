package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.strategy.StrategySelector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// should implement Notifier
@Component
public class CommunityNotifier implements Notifier {
    private StrategySelector strategySelector;

    @Override
    public void notify(Notification notification) {
        UUID target = notification.getReceiverId();
//        TODO: comunication  get all users inside the target comunity id
//        UUID list
        List<UUID> communityUsersId = new ArrayList<>();

        for (UUID userId : communityUsersId) {
            strategySelector.selectStrategy(String.valueOf(userId)).deliver(notification);
        }

    }


}