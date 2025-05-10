package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.strategy.StrategySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// should implement Notifier
@Component
public class ThreadNotifier implements Notifier {
    @Autowired
    private StrategySelector strategySelector;
    private UserNotifyRepository userNotifyRepository;

    @Override
    public void notify(Notification notification) {
//        UUID target = notification.getReceiverId();
//        TODO: comunication  get all users inside the target thread id
        List<UUID> threadUsersId = notification.getRecieversId();


        for (UUID userId : threadUsersId) {
            UserNotification userNotification = new UserNotification(userId, notification);
            userNotifyRepository.save(userNotification);
            strategySelector.performDelivery(notification);
        }
    }
}