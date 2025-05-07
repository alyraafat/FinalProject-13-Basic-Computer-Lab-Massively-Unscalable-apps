package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.services.Factory.Notifier;
import org.springframework.stereotype.Component;

// should implement Notifier
@Component
public class UserNotifier implements Notifier {
    private DeliveryChannel deliveryChannel;

    @Override
    public void notify(Notification request) {

    }

}
