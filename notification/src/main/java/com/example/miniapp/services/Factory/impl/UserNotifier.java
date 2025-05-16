package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.entity.Notification;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.repositories.UserNotifyRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.strategy.StrategySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


// should implement Notifier
@Component
public class UserNotifier implements Notifier {
    @Autowired
    private StrategySelector strategySelector;
    @Autowired
    private UserNotifyRepository userNotifyRepository;
//    private UserNotification userNotification;

    @Override
    public void notify(Notification notification) {
        // Create and save user notification first
        UserNotification userNotification = new UserNotification(notification.getReceiversId().get(0), notification);

        userNotifyRepository.save(userNotification);

            strategySelector.performDelivery(userNotification);

    }

}