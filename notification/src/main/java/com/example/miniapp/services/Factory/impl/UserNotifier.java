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

//        UUID target = notification.getReceiverId();
//        DeliveryStrategy deliveryStrategy = strategySelect.selectStrategy(String.valueOf(target));
//
//        StrategySelector strategySelector = new StrategySelector( deliveryStrategy);
//
//        strategySelector.performDelivery(notification);

        userNotification.setUserId(notification.getReceiverId());
        userNotification.setNotification(notification);
//        userNotification.setReadAt("null");
//        TODO: to be continued (rest of attributes or make constructor instead)

        userNotifyRepository.save(userNotification);
        strategySelector.performDelivery(notification);

    }

}