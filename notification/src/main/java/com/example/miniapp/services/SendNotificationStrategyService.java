package com.example.miniapp.services;


import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationStrategyService {
    private DeliveryStrategy deliveryStrategy;

    public SendNotificationStrategyService(@Qualifier("emailStrategy") DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public void deliver(UserNotification userNotification) {
        deliveryStrategy.deliver(userNotification);
    }

}
