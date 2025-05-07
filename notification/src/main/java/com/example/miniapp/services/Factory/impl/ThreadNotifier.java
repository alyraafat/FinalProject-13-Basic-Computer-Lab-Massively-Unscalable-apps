package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.services.Factory.Notifier;
import org.springframework.stereotype.Component;

// should implement Notifier
@Component
public class ThreadNotifier implements Notifier {
    private DeliveryChannel deliveryChannel;
    @Override
    public void notify(NotificationRequest request) {
//        TODO: use the repository, construct the entities needed


//        TODO: pass to the strategy

    }

    public void setDeliveryStrategy(DeliveryChannel strategy) {
        this.deliveryChannel = strategy;
    }

    public DeliveryChannel getDeliveryChannel() {
        return deliveryChannel;
    }
}
